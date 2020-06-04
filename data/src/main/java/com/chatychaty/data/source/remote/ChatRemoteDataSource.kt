package com.chatychaty.data.source.remote

import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Response

interface ChatRemoteDataSource {

    suspend fun createChat(token: String, username: String): Response

    suspend fun getChats(token: String): List<Chat>

    suspend fun checkUpdates(token: String): Response
}