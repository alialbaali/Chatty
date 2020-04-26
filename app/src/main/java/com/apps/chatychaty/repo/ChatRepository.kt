package com.apps.chatychaty.repo

import androidx.lifecycle.LiveData
import com.apps.chatychaty.di.AUTH_SCHEME
import com.apps.chatychaty.local.ChatDao
import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.model.Response
import com.apps.chatychaty.remote.ChatClient
import com.apps.chatychaty.token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val chatClient: ChatClient, private val chatDao: ChatDao) {

    suspend fun insertChatClient(token: String, username: String): String? {
        return withContext(Dispatchers.IO) {
            val response = chatClient.insertChat(AUTH_SCHEME.plus(token), username)
            if (response.condition) {
                chatDao.insertChat(Chat(response.chatId!!, response.user!!))
                null
            } else {
                response.error
            }
        }
    }

    suspend fun getChatsClient(token: String) {
        withContext(Dispatchers.IO) {
            val chats = chatClient.getChats(AUTH_SCHEME.plus(token))
            chatDao.updateChats(chats)
        }
    }

    suspend fun getChatsDao(): LiveData<List<Chat>> {
        return withContext(Dispatchers.Main) {
            chatDao.getChats()
        }
    }

    suspend fun checkUpdates(): Response {
        return withContext(Dispatchers.IO) {
            chatClient.checkUpdates(AUTH_SCHEME.plus(token))
        }
    }
}