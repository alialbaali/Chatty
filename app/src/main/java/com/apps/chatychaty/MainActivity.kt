package com.apps.chatychaty

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.apps.chatychaty.database.AppDatabase
import com.apps.chatychaty.network.DAOs

const val DURATION = 500L
var token: String? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        DAOs.chatDao = AppDatabase.getInstance(this).chatDao

        DAOs.messageDao = AppDatabase.getInstance(this).messageDao

        token = this.getPref("token")

        if (token != null) {
            this.findNavController(R.id.nav_host_fragment_container).let { nav ->
                nav.popBackStack()
                nav.navigate(R.id.listFragment)
            }
        }
    }
}
