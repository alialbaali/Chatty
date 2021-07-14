package com.chatychaty.remote.message

import com.chatychaty.remote.user.AUTH_HEADER
import retrofit2.http.*

private const val LAST_MESSAGE_DATE_TIME = "lastMessageDateTime"
private const val LAST_MESSAGE_STATUS_DATE_TIME = "lastMessageStatusDateTime"
private const val MESSAGE_ID = "messageId"

interface RemoteMessageDataSource {

    @GET("v1/Message/NewMessages")
    suspend fun getMessages(@Header(AUTH_HEADER) token: String, @Header(LAST_MESSAGE_DATE_TIME) lastMessageDateTime: String?): List<RemoteMessage>

    @GET("v1/Message/MessageStatus")
    suspend fun getMessageStatus(
        @Header(AUTH_HEADER) token: String,
        @Query(LAST_MESSAGE_STATUS_DATE_TIME) lastMessageStatusDateTime: String?
    ): List<RemoteMessage>

    @POST("v1/Message/Message")
    suspend fun createMessage(@Header(AUTH_HEADER) token: String, @Body newMessageBody: NewMessageBody): RemoteMessage

    @GET("v1/Message/Delivered")
    suspend fun isMessageDelivered(@Header(AUTH_HEADER) token: String, @Header(MESSAGE_ID) messageId: String): Boolean

}