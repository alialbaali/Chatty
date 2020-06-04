package com.chatychaty.data.source.remote

import com.chatychaty.domain.model.Message

interface MessageRemoteDataSource {

    suspend fun createMessage(token: String, message: Message): Message

    suspend fun getMessages( token: String, lastMessageId: Int): List<Message>

    suspend fun isMessageDelivered( token: String,  messageId: Int): Boolean
}