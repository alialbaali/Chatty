package com.alialbaali.chatychaty

import android.app.Application
import com.alialbaali.di.localModule
import com.alialbaali.di.remoteModule
import com.alialbaali.di.repositoryModule
import com.alialbaali.chatychaty.viewModel.ProfileViewModel
import com.alialbaali.chatychaty.viewModel.SearchViewModel
import com.alialbaali.chatychaty.viewModel.SharedViewModel
import com.alialbaali.chatychaty.viewModel.SignSharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

private val job = SupervisorJob()

val applicationScope = CoroutineScope(job + Dispatchers.Default)

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(appModule, repositoryModule, localModule, remoteModule)
        }

//        val workConstraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val periodicWork =
//            PeriodicWorkRequestBuilder<CheckUpdatesWork>(1, TimeUnit.SECONDS)
//                .setConstraints(workConstraints)
//                .build()
//
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            CheckUpdatesWork.WORK_NAME,
//            ExistingPeriodicWorkPolicy.REPLACE,
//            periodicWork
//        )
    }
}

val appModule = module {
    viewModel { SignSharedViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SharedViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
}