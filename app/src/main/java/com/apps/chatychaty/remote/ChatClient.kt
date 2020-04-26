package com.apps.chatychaty.remote

import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.model.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ChatClient {

    @GET("v1/Profile/GetUser")
    suspend fun insertChat(
        @Header("Authorization") token: String,
        @Header("UserName") username: String
    ): Response

    @GET("v1/Profile/GetChats")
    suspend fun getChats(@Header("Authorization") token: String): List<Chat>

    @GET("v2/Notification/CheckForUpdates")
    suspend fun checkUpdates(@Header("Authorization") token: String): Response
}