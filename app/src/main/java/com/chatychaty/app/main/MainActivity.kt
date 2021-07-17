package com.chatychaty.app.main

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.chatychaty.app.R
import com.chatychaty.app.databinding.ActivityMainBinding
import com.chatychaty.app.util.SyncWorker
import com.chatychaty.app.util.UiState
import com.chatychaty.app.notification.createNotificationChannel
import com.chatychaty.domain.model.Theme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()
    private val notificationManager by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val workManager by lazy { WorkManager.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        notificationManager.createNotificationChannel()
        collectState()
        setupSystemBarsVisibility()
        handleIntentContent()
    }

    private fun handleIntentContent() {
        if (intent?.action == Intent.ACTION_SEND) {
            intent
                .getStringExtra(Intent.EXTRA_TEXT)
                ?.let {
                    findNavController(R.id.nav_host_fragment_container)
                        .navigate(R.id.shareFragment, bundleOf("body" to it))
                }
        }
    }

    override fun onStart() {
        super.onStart()
        workManager.cancelAllWork()
    }

    @Suppress("DEPRECATION")
    private fun setupSystemBarsVisibility() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility = 0
            Configuration.UI_MODE_NIGHT_NO -> {
                val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                else
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                window.decorView.systemUiVisibility = flags
            }
        }
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