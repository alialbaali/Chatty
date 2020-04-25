package com.apps.chatychaty.di

import android.content.Context
import android.content.SharedPreferences
import com.apps.chatychaty.local.AppDatabase
import com.apps.chatychaty.remote.ChatClient
import com.apps.chatychaty.remote.MessageClient
import com.apps.chatychaty.remote.UserClient
import com.apps.chatychaty.repo.ChatRepository
import com.apps.chatychaty.repo.MessageRepository
import com.apps.chatychaty.repo.UserRepository
import com.apps.chatychaty.viewModel.ProfileViewModel
import com.apps.chatychaty.viewModel.SearchViewModel
import com.apps.chatychaty.viewModel.SharedViewModel
import com.apps.chatychaty.viewModel.SignSharedViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val SHARED_PREFERENCES_NAME = "ChatyChaty Shared Preferences"

private const val BASE_URL = "https://chatychaty0.herokuapp.com/api/"

const val AUTH_SCHEME = "Bearer "

const val AUTH_HEADER = "Authorization"

private val TIMEOUT = 60L

private val TIMEUNIT = TimeUnit.SECONDS

val appModule = module {

    single { MessageRepository(get(), get()) }

    single { ChatRepository(get(), get()) }

    single { UserRepository(get(), get()) }

    viewModel { ProfileViewModel(get()) }

    viewModel { SearchViewModel(get()) }

    viewModel { SignSharedViewModel(get()) }

    viewModel { SharedViewModel(get(), get()) }

    single { AppDatabase.getInstance(androidContext()).chatDao }

    single { AppDatabase.getInstance(androidContext()).messageDao }

    single { androidContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE) as SharedPreferences }

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

// object ErrorConverter {
//    val converter: Converter<ResponseBody, ErrorResponse> =
//        retrofit.responseBodyConverter(ErrorResponse::class.java, arrayOf())
//}