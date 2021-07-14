package com.chatychaty.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.chatychaty.data.repository.ChatRepositoryImpl
import com.chatychaty.data.repository.MessageRepositoryImpl
import com.chatychaty.data.repository.UserRepositoryImpl
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository
import com.chatychaty.domain.repository.UserRepository
import com.chatychaty.local.*
import com.chatychaty.remote.chat.RemoteChatDataSource
import com.chatychaty.remote.message.RemoteMessageDataSource
import com.chatychaty.remote.message.SignalRHub
import com.chatychaty.remote.message.SignalRHubImpl
import com.chatychaty.remote.user.RemoteUserDataSource
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

    single<HttpLoggingInterceptor> { HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY } }

    single<Moshi> { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
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

    single<Retrofit> {
        val moshi = get<Moshi>()
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        Retrofit
            .Builder()
            .addConverterFactory(moshiConverterFactory)
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .build()
    }

    single<RemoteMessageDataSource> { (get() as Retrofit).create(RemoteMessageDataSource::class.java) }

    single<RemoteChatDataSource> { (get() as Retrofit).create(RemoteChatDataSource::class.java) }

    single<RemoteUserDataSource> { (get() as Retrofit).create(RemoteUserDataSource::class.java) }

    single<SignalRHub> { SignalRHubImpl() }
}

val repositoryModule = module {
    single<MessageRepository> {
        MessageRepositoryImpl(
            get<RemoteMessageDataSource>(),
            get<MessageDao>(),
            get<UserDao>(),
            signalRHub = get<SignalRHub>(),
        )
    }

    single<ChatRepository> {
        ChatRepositoryImpl(
            get<RemoteChatDataSource>(),
            get<ChatDao>(),
            get<UserDao>(),
            signalRHub = get<SignalRHub>(),
        )
    }

    single<UserRepository> { UserRepositoryImpl(get<UserDao>(), get<RemoteUserDataSource>()) }
}

val localModule = module {
    single<ChatDao> { AppDatabase.getInstance(androidContext()).chatDao }

    single<MessageDao> { AppDatabase.getInstance(androidContext()).messageDao }

    single<SharedPreferences> { androidContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE) }

    single<UserDao> { UserDaoImpl(androidContext().dataStore) }

}

private val Context.dataStore by preferencesDataStore("ChatyChaty")