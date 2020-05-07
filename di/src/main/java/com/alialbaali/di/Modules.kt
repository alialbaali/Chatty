package com.alialbaali.di

import android.content.Context
import android.content.SharedPreferences
import com.alialbaali.local.AppDatabase
import com.alialbaali.remote.ChatClient
import com.alialbaali.remote.MessageClient
import com.alialbaali.remote.UserClient
import com.alialbaali.repository.ChatRepository
import com.alialbaali.repository.MessageRepository
import com.alialbaali.repository.UserRepository
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

private val TIMEOUT = 60L

private val TIMEUNIT = TimeUnit.SECONDS

val remoteModule = module {

    single { HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY } }

    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() as Moshi }

    single {
        OkHttpClient.Builder().addInterceptor(get() as HttpLoggingInterceptor).callTimeout(TIMEOUT, TIMEUNIT).readTimeout(TIMEOUT, TIMEUNIT)
            .writeTimeout(TIMEOUT, TIMEUNIT).connectTimeout(TIMEOUT, TIMEUNIT).build()
    }

    single { Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(get())).baseUrl(BASE_URL).client(get()).build() as Retrofit }

    single { (get() as Retrofit).create(MessageClient::class.java) }

    single { (get() as Retrofit).create(ChatClient::class.java) }

    single { (get() as Retrofit).create(UserClient::class.java) }

}

val repositoryModule = module {
    single { MessageRepository(get(), get(), get()) }

    single { ChatRepository(get(), get(), get()) }

    single { UserRepository(get(), get()) }
}

val localModule = module {
    single { AppDatabase.getInstance(androidContext()).chatDao }

    single { AppDatabase.getInstance(androidContext()).messageDao }

    single { androidContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE) as SharedPreferences }
}