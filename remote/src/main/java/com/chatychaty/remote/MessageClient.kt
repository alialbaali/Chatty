package com.chatychaty.remote

import com.chatychaty.data.source.remote.MessageRemoteDataSource
import com.chatychaty.domain.model.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MessageClient: MessageRemoteDataSource {

    @POST("v2/Message/SendMessage")
    override suspend fun createMessage(@Header(AUTH_HEADER) token: String, @Body message: Message): Message

    @GET("v2/Message/GetNewMessages")
    override suspend fun getMessages(@Header(AUTH_HEADER) token: String, @Header("LastMessageId") lastMessageId: Int): List<Message>

    @GET("v2/Message/CheckDelivered")
    override suspend fun isMessageDelivered(@Header(AUTH_HEADER) token: String, @Header("MessageId") messageId: Int): Boolean
}