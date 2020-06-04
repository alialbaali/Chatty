package com.chatychaty.data.source.local

import com.chatychaty.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageLocalDataSource {

    suspend fun createMessage(message: Message)

    fun getMessages(chatId: Int): Flow<List<Message>>

    suspend fun count(): Int

    suspend fun updateMessages(messages: List<Message>)

    suspend fun updateDelivered()

    fun getLastMessage(chatId: Int): Flow<String>

    suspend fun deleteMessages()
}