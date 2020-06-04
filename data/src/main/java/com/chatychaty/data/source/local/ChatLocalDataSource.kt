package com.chatychaty.data.source.local

import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatLocalDataSource {
    fun createChat(chat: Chat)

    fun getChatById(chatId: Int): Flow<Chat>

    fun getChats(): Flow<List<Chat>>

    fun updateChats(chats: List<Chat>)

    fun deleteChats()
}