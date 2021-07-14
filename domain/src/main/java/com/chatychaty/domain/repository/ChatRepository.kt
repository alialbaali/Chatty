package com.chatychaty.domain.repository

import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val dispatcher: CoroutineDispatcher

    fun getChats(): Flow<List<Chat>>

    fun getArchivedChats(): Flow<List<Chat>>

    fun getChatById(chatId: String): Flow<Chat>

    suspend fun createChat(username: String): Result<Chat>

    suspend fun archiveChat(chatId: String)

    suspend fun unarchiveChat(chatId: String)

    suspend fun pinChat(chatId: String)

    suspend fun unpinChat(chatId: String)

    suspend fun muteChat(chatId: String)

    suspend fun unmuteChat(chatId: String)

    suspend fun refreshChats()

    suspend fun syncChats()

    suspend fun deleteChats()
}