package com.chatychaty.remote

import com.chatychaty.data.source.remote.ChatRemoteDataSource
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Response
import retrofit2.http.GET
import retrofit2.http.Header

private const val USERNAME_HEADER = "UserName"

interface ChatClient: ChatRemoteDataSource {

    @GET("v1/Profile/GetUser")
    override suspend fun createChat(@Header(AUTH_HEADER) token: String, @Header(USERNAME_HEADER) username: String): Response

    @GET("v1/Profile/GetChats")
    override suspend fun getChats(@Header(AUTH_HEADER) token: String): List<Chat>

    @GET("v2/Notification/CheckForUpdates")
    override suspend fun checkUpdates(@Header(AUTH_HEADER) token: String): Response
}