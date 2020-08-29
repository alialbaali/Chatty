package com.chatychaty.remote

import com.chatychaty.data.source.remote.MessageRemoteDataSource
import com.chatychaty.data.source.remote.schema.Response
import com.chatychaty.domain.model.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val LAST_MESSAGE_ID_HEADER = "lastMessageId"
private const val MESSAGE_ID = "messageId"

interface MessageClient : MessageRemoteDataSource {

    @POST("v3/Message/Message")
    override suspend fun createMessage(@Header(AUTH_HEADER) token: String, @Body message: Message): Response<Message>

    @GET("v3/Message/NewMessages")
    override suspend fun getMessages(@Header(AUTH_HEADER) token: String, @Header(LAST_MESSAGE_ID_HEADER) lastMessageId: Int): Response<List<Message>>

    @GET("v3/Message/Delivered")
    override suspend fun isMessageDelivered(@Header(AUTH_HEADER) token: String, @Header(MESSAGE_ID) messageId: Int): Response<Boolean>

}