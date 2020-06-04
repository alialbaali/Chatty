package com.chatychaty.domain.repository

import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val dispatcher: CoroutineDispatcher

    val token: String?

    suspend fun createChat(username: String): Result<Chat>

    suspend fun getChats(): Result<Flow<List<Chat>>>

    suspend fun getRemoteChats()

    suspend fun checkUpdates(): Pair<Boolean, Boolean>

    suspend fun deleteChats()

    fun getChatById(chatId: Int): Result<Flow<Chat>>
}