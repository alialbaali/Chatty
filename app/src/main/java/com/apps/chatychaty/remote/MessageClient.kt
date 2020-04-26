package com.apps.chatychaty.remote

import com.apps.chatychaty.model.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MessageClient {

    @POST("v2/Message/SendMessage")
    suspend fun insertMessage(
        @Header("Authorization") token: String,
        @Body message: Message
    ): Message

    @GET("v2/Message/GetNewMessages")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Header("LastMessageId") lastMessageId: Int
    ): List<Message>

    @GET("v2/Message/CheckDelivered")
    suspend fun isMessageDelivered(
        @Header("Authorization") token: String,
        @Header("MessageId") messageId: Int
    ): Boolean

}