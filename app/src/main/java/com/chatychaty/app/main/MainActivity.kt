package com.chatychaty.app.main

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.chatychaty.app.R
import com.chatychaty.app.util.SyncWorker
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.createNotificationChannel
import com.chatychaty.domain.model.Theme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val notificationManager by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val workManager by lazy { WorkManager.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager.createNotificationChannel()
        collectState()
    }

    override fun onStart() {
        super.onStart()
        workManager.cancelAllWork()
    }

    @SuppressLint("RestrictedApi")
    private fun setupSyncWorker() {
        val constraints = Constraints().apply {
            requiredNetworkType = NetworkType.CONNECTED
        }

        val syncWorker = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(syncWorker)
    }

    private fun collectState() {
        viewModel.currentUser
            .onEach {
                if (it is UiState.Success) {
                    findNavController(R.id.nav_host_fragment_container).also { nav ->
                        nav.popBackStack()
                        nav.navigate(R.id.chatListFragment)
                    }
                    viewModel.refreshData()
                    viewModel.connectHub()
                }
            }
            .launchIn(lifecycleScope)

        viewModel.theme
            .onEach { state ->
                if (state is UiState.Success) {
                    when (state.value) {
                        Theme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        setupSyncWorker()
    }

    override fun onDestroy() {
        super.onDestroy()
//        viewModel.disconnectHub()
    }

}