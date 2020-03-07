package com.apps.chatychaty.network

import com.apps.chatychaty.repo.MessageRepository
import com.apps.chatychaty.repo.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://chatychaty0.herokuapp.com/api/v1"

private val logger = HttpLoggingInterceptor().also {
    HttpLoggingInterceptor.Level.BODY
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient by lazy {
    OkHttpClient.Builder()
        .addInterceptor(logger)
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

private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6ImFsaWFsYmFhbGkiLCJqdGkiOiIwY2E3NDdiZC03NTdhLTQ3NmYtYjMzOS01MTIzMTgwNGI5OGMiLCJuYmYiOjE1ODM1ODU2OTYsImV4cCI6MTU4NDE5MDQ5NiwiaWF0IjoxNTgzNTg1Njk2LCJpc3MiOiJodHRwczovL2NoYXR5Y2hhdHkwLmhlcm9rdWFwcC5jb20vIiwiYXVkIjoiaHR0cHM6Ly9jaGF0eWNoYXR5MC5oZXJva3VhcHAuY29tLyJ9.w5DlTjtxy652W1YD3WqgS44Nk72rmNSRIq6TVOCQBq8"
