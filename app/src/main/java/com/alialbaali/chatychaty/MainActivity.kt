package com.alialbaali.chatychaty

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.alialbaali.chatychaty.databinding.ActivityMainBinding
import com.alialbaali.chatychaty.util.getPref
import com.alialbaali.repository.TOKEN
import org.koin.android.ext.android.inject

const val DURATION = 350L
var token: String? = null

class MainActivity : AppCompatActivity() {

    private val sharedPreferences by inject<SharedPreferences>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(CheckUpdatesWork.WORK_NAME)
//            .observe(this, Observer {
//                val worker = it.first()
//                Timber.i(
//                    """
//                ID = ${worker.id}
//               Output Data = ${worker.outputData}
//                Progress = ${worker.progress}
//                run Attempt Count = ${worker.runAttemptCount}
//                State = ${worker.state}
//               Tags =  ${worker.tags}
//            """.trimIndent()
//                )
//            })


        val followSystemTheme = resources.getString(R.string.follow_system_theme)
        val lightTheme = resources.getString(R.string.light_theme)
        val darkTheme = resources.getString(R.string.dark_theme)

        when (this.getPref("theme")) {
            followSystemTheme ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

            lightTheme ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            darkTheme ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }

        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES ->
                window.decorView.systemUiVisibility = 0

            Configuration.UI_MODE_NIGHT_NO
            -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }

        token = sharedPreferences.getString(TOKEN, null)

        if (token != null) {
            this.findNavController(R.id.nav_host_fragment_container).let { nav ->
                nav.popBackStack()
                nav.navigate(R.id.listFragment)
            }
        }
    }
}
