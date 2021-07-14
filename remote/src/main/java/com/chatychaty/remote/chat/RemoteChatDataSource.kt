package com.chatychaty.remote.chat

import com.chatychaty.remote.user.AUTH_HEADER
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val USERNAME = "userName"

interface RemoteChatDataSource {

    @GET("v1/Profile/Chats")
    suspend fun getChats(@Header(AUTH_HEADER) token: String): List<RemoteChat>

    @GET("v1/Profile/User")
    suspend fun createChat(@Header(AUTH_HEADER) token: String, @Query(USERNAME) username: String): RemoteChat

}