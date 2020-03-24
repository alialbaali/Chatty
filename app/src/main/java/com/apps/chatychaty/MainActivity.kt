package com.apps.chatychaty

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

const val DURATION = 500L

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mode = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

////        window.statusBarColor = resources.getColor(R.color.colorOnPrimary_900)
//
//        when (mode) {
//            Configuration.UI_MODE_NIGHT_YES -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                }
//            }
//
//            Configuration.UI_MODE_NIGHT_NO -> {
////                window.decorView.systemUiVisibility = 0
//            }
//        }

        getPreferences(Context.MODE_PRIVATE).let {
            val token = it.getString("token", null)

            if (token != null) {
                this.findNavController(R.id.nav_host_fragment_container).let { nav ->
                    nav.popBackStack()
                    nav.navigate(R.id.listFragment)
                }
            }

        }
    }
}
