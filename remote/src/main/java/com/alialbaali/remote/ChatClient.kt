package com.alialbaali.remote

import com.alialbaali.model.Chat
import com.alialbaali.model.Response
import retrofit2.http.GET
import retrofit2.http.Header

private const val USERNAME_HEADER = "UserName"

interface ChatClient {

    @GET("v1/Profile/GetUser")
    suspend fun insertChat(@Header(AUTH_HEADER) token: String, @Header(USERNAME_HEADER) username: String): Response

    @GET("v1/Profile/GetChats")
    suspend fun getChats(@Header(AUTH_HEADER) token: String): List<Chat>

    @GET("v2/Notification/CheckForUpdates")
    suspend fun checkUpdates(@Header(AUTH_HEADER) token: String): Response
}