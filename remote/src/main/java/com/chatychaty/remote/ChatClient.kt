package com.chatychaty.remote

import com.chatychaty.data.source.remote.ChatRemoteDataSource
import com.chatychaty.data.source.remote.schema.Response
import com.chatychaty.data.source.remote.schema.UpdateResponse
import com.chatychaty.domain.model.Chat
import retrofit2.http.GET
import retrofit2.http.Header

private const val USERNAME_HEADER = "UserName"

interface ChatClient : ChatRemoteDataSource {

    @GET("v3/Profile/User")
    override suspend fun createChat(@Header(AUTH_HEADER) token: String, @Header(USERNAME_HEADER) username: String): Response<Chat>

    @GET("v3/Profile/Chats")
    override suspend fun getChats(@Header(AUTH_HEADER) token: String): Response<List<Chat>>

    @GET("v3/Notification/Updates")
    override suspend fun checkUpdates(@Header(AUTH_HEADER) token: String): Response<UpdateResponse>

}