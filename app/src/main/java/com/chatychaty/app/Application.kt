package com.chatychaty.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.chatychaty.app.chat.ChatListViewModel
import com.chatychaty.app.chat.archive.ChatListArchiveViewModel
import com.chatychaty.app.login.SignSharedViewModel
import com.chatychaty.app.main.MainViewModel
import com.chatychaty.app.message.MessageItemViewModel
import com.chatychaty.app.message.MessageListViewModel
import com.chatychaty.app.profile.ProfileViewModel
import com.chatychaty.app.search.SearchViewModel
import com.chatychaty.app.user.UserViewModel
import com.chatychaty.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                appModule,
                localModule,
                remoteModule,
                repositoryModule,
            )
        }
    }
}

private val appModule = module {
    viewModel { SignSharedViewModel(get()) }
    viewModel { UserViewModel(get(), get(), get()) }
    viewModel { parameters -> MessageListViewModel(get(), get(), parameters.get()) }
    viewModel { parameters -> ProfileViewModel(get(), parameters.get()) }
    viewModel { MessageItemViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { ChatListViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { ChatListArchiveViewModel(get(), get()) }
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
}