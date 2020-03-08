package com.apps.chatychaty.network

import com.apps.chatychaty.repo.MessageRepository
import com.apps.chatychaty.repo.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://chatychaty0.herokuapp.com/api/v1/"

private val logger = HttpLoggingInterceptor().also {
    it.level = HttpLoggingInterceptor.Level.BODY
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient by lazy {
    OkHttpClient.Builder()
        .addInterceptor(logger)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
}

private val retrofit by lazy {
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
}

private object Clients {
    internal val userClient by lazy {
        retrofit.create(UserClient::class.java)
    }

    internal val messageClient by lazy {
        retrofit.create(MessageClient::class.java)
    }
}

internal object Repos {
    internal val userRepository by lazy {
        UserRepository(Clients.userClient)
    }

    internal val messageRepository by lazy {
        MessageRepository(Clients.messageClient)
    }
}