package com.chatychaty.domain.repository

import com.chatychaty.domain.model.Message
import kotlinx.coroutines.flow.Flow


interface MessageRepository {

    val token: String?

    suspend fun createMessage(message: Message): Result<Message>

    suspend fun getRemoteMessages(): Result<List<Message>>

    suspend fun getMessages(chatId: Int): Flow<Result<List<Message>>>

    suspend fun isMessageDelivered(messageId: Int): Result<Unit>

    suspend fun getLastMessage(chatId: Int): Result<Flow<String>>

    suspend fun deleteMessages()
}