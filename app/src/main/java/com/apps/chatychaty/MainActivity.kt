package com.apps.chatychaty

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPreferences(Context.MODE_PRIVATE).let {
            val username = it.getString("username", null)
            val token = it.getString("token", null)

            if (username != null && token != null) {
                this.findNavController(R.id.nav_host_fragment_container).let {nav ->
                    nav.popBackStack()
                    nav.navigate(R.id.chatFragment)
                }
            }

        }
    }
}
