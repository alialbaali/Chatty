package com.chatychaty.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.chatychaty.app.login.SignSharedViewModel
import com.chatychaty.app.user.UserViewModel
import com.chatychaty.app.search.SearchViewModel
import com.chatychaty.di.*
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
            modules(
                appModule,
                localModule,
                remoteModule,
                repositoryModule,
                userUseCaseModule,
                chatUseCaseModule,
                messageUseCaseModule
            )
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
    viewModel { UserViewModel(get()) }
    viewModel { SharedViewModel(get(), get()) }
    viewModel { SearchViewModel() }
    viewModel { MainViewModel(get()) }
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
}