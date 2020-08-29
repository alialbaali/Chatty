package com.chatychaty.di

import android.content.Context
import android.content.SharedPreferences
import com.chatychaty.data.repository.ChatRepositoryImpl
import com.chatychaty.data.repository.MessageRepositoryImpl
import com.chatychaty.data.repository.UserRepositoryImpl
import com.chatychaty.domain.interactor.chat.*
import com.chatychaty.domain.interactor.message.*
import com.chatychaty.domain.interactor.user.*
import com.chatychaty.domain.repository.UserRepository
import com.chatychaty.domain.usecase.chat.CheckUpdates
import com.chatychaty.local.AppDatabase
import com.chatychaty.local.ChatDao
import com.chatychaty.local.MessageDao
import com.chatychaty.local.UserDao
import com.chatychaty.remote.ChatClient
import com.chatychaty.remote.MessageClient
import com.chatychaty.remote.UserClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val SHARED_PREFERENCES_NAME = "ChatyChaty Shared Preferences"

private const val BASE_URL = "https://chatychaty0.herokuapp.com/api/"

private const val TIMEOUT = 60L

private val TIMEUNIT = TimeUnit.SECONDS

val remoteModule = module {

    single { HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY } }

    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() as Moshi }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get() as HttpLoggingInterceptor)
//            .addNetworkInterceptor(object : NetworkConnectionInterceptor() {
//
//                lateinit var listener: NetworkConnectionListener
//
//                override fun isNetworkAvailable(): Boolean {
//                    val cm = androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//                    return cm.isNetworkAvailable()
//                }
//
//                override fun onNetworkUnavailable() {
//                    listener.onInternetUnavailable()
//                }
//            })
            .callTimeout(TIMEOUT, TIMEUNIT)
            .readTimeout(TIMEOUT, TIMEUNIT)
            .writeTimeout(TIMEOUT, TIMEUNIT)
            .connectTimeout(TIMEOUT, TIMEUNIT)
            .build()
    }

    single { Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(get())).baseUrl(BASE_URL).client(get()).build() as Retrofit }

    single { (get() as Retrofit).create(MessageClient::class.java) }

    single { (get() as Retrofit).create(ChatClient::class.java) }

    single { (get() as Retrofit).create(UserClient::class.java) }
}

val repositoryModule = module {
    single {
        MessageRepositoryImpl(
            get() as MessageClient,
            get() as MessageDao,
            get() as UserDao
        )
    }

    single {
        ChatRepositoryImpl(
            get() as ChatClient,
            get() as ChatDao,
            get() as UserDao
        )
    }

    single { UserRepositoryImpl(get() as UserDao, get() as UserClient) }
}

val localModule = module {
    single { AppDatabase.getInstance(androidContext()).chatDao }

    single { AppDatabase.getInstance(androidContext()).messageDao }

    single { androidContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE) as SharedPreferences }

    single { UserDao(get()) }
}

val chatUseCaseModule = module {

    single { ChatUseCases(get(), get(), get(), get(), get(), get()) }

    single { CreateChat(get() as ChatRepositoryImpl) }

    single { GetChats(get() as ChatRepositoryImpl) }

    single { CheckUpdates(get() as ChatRepositoryImpl, get() as MessageRepositoryImpl) }

    single { GetMessages(get() as MessageRepositoryImpl) }

    single { GetRemoteChats(get() as ChatRepositoryImpl) }

    single { GetChatById(get() as ChatRepositoryImpl) }
}

val messageUseCaseModule = module {

    single { MessageUseCase(get(), get(), get()) }

    single { CreateMessage(get() as MessageRepositoryImpl) }

    single { GetLastMessage(get() as MessageRepositoryImpl) }

    single { GetRemoteMessages(get() as MessageRepositoryImpl) }

}

val userUseCaseModule = module {

    single { UserUseCase(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }

    single { SignUp(get() as UserRepositoryImpl) }

    single { SignIn(get() as UserRepositoryImpl) }

    single { UpdatePhoto(get() as UserRepositoryImpl) }

    single { UpdateName(get() as UserRepositoryImpl) }

    single { GetTheme(get() as UserRepositoryImpl) }

    single { SetTheme(get() as UserRepositoryImpl) }

    single {
        SignOut(
            get() as UserRepositoryImpl,
            get() as ChatRepositoryImpl,
            get() as MessageRepositoryImpl
        )
    }
    single { GetChatUser(get() as ChatRepositoryImpl) }

    single { GetUser(get() as UserRepositoryImpl) }

    single { SubmitFeedback(get() as UserRepositoryImpl)  }

    single { DeleteAccount(get() as UserRepositoryImpl, get() as ChatRepositoryImpl, get() as MessageRepositoryImpl)  }
}
