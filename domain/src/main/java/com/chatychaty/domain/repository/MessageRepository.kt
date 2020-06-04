package com.chatychaty.domain.repository

import com.chatychaty.domain.model.Message
import kotlinx.coroutines.flow.Flow


interface MessageRepository {

    val token: String?

    suspend fun createMessage(message: Message)

    suspend fun getRemoteMessages()

    suspend fun getMessages(chatId: Int): Result<Flow<List<Message>>>

    suspend fun isMessageDelivered(messageId: Int)

    suspend fun getLastMessage(chatId: Int): Result<Flow<String>>

    suspend fun deleteMessages()
}