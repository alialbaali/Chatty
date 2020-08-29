package com.chatychaty.app

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.chatychaty.domain.repository.TOKEN
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val sharedPreferences by inject<SharedPreferences>()


    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

//
//        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
//            Configuration.UI_MODE_NIGHT_YES ->
//                window.decorView.systemUiVisibility = 0
//
//            Configuration.UI_MODE_NIGHT_NO
//            -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                }
//            }
//        }

        if (sharedPreferences.getString(TOKEN, null) != null) {
            this.findNavController(R.id.nav_host_fragment_container).let { nav ->
                nav.popBackStack()
                nav.navigate(R.id.listFragment)
            }
        }
    }
}
