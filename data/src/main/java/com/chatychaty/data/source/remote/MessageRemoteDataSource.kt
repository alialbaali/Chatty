package com.chatychaty.data.source.remote

import com.chatychaty.data.source.remote.schema.Response
import com.chatychaty.domain.model.Message

interface MessageRemoteDataSource {

    suspend fun createMessage(token: String, message: Message): Response<Message>

    suspend fun getMessages( token: String, lastMessageId: Int): Response<List<Message>>

    suspend fun isMessageDelivered( token: String,  messageId: Int): Response<Boolean>
}