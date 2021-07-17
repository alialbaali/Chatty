package com.chatychaty.domain.repository

import com.chatychaty.domain.model.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow


interface MessageRepository {

    val dispatcher: CoroutineDispatcher

    suspend fun connectHub()

    fun getMessages(chatId: String): Flow<List<Message>>

    fun getMessageById(messageId: String): Flow<Message>

    fun getNewMessages(): Flow<List<Message>>

    fun getLastMessages(): Flow<List<Message>>

    suspend fun createMessage(message: Message): Result<Message>

    suspend fun isMessageDelivered(messageId: String): Result<Boolean>

    suspend fun updateNewMessages(chatId: String)

    suspend fun refreshMessages()

    suspend fun syncMessages()

    suspend fun deleteMessages()

    suspend fun disconnectHub()
}