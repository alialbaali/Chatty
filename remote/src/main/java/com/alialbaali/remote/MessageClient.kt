package com.alialbaali.remote

import com.alialbaali.model.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MessageClient {

    @POST("v2/Message/SendMessage")
    suspend fun insertMessage(@Header(AUTH_HEADER) token: String, @Body message: Message): Message

    @GET("v2/Message/GetNewMessages")
    suspend fun getMessages(@Header(AUTH_HEADER) token: String, @Header("LastMessageId") lastMessageId: Int): List<Message>

    @GET("v2/Message/CheckDelivered")
    suspend fun isMessageDelivered(@Header(AUTH_HEADER) token: String, @Header("MessageId") messageId: Int): Boolean
}