package com.apps.chatychaty.repo

import androidx.lifecycle.LiveData
import com.apps.chatychaty.database.ChatDao
import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.model.Response
import com.apps.chatychaty.network.AUTH_SCHEME
import com.apps.chatychaty.network.ChatClient
import com.apps.chatychaty.token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ChatRepository(private val chatClient: ChatClient, private val chatDao: ChatDao) {

    internal suspend fun insertChatClient(token: String, username: String): Response {
        return withContext(Dispatchers.IO) {
            chatClient.insertChat(AUTH_SCHEME.plus(token), username)
        }
    }

    internal suspend fun getChatsClient(token: String): List<Chat> {
        return withContext(Dispatchers.IO) {
            chatClient.getChats(AUTH_SCHEME.plus(token))
        }
    }

    internal suspend fun getChatsDao(): LiveData<List<Chat>> {
        return withContext(Dispatchers.Main) {
            chatDao.getChats()
        }
    }

    internal suspend fun insertChatDao(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatDao.insertChat(chat)
        }
    }

    internal suspend fun updateChatsDao(chats: List<Chat>) {
        withContext(Dispatchers.IO) {
            chatDao.updateChats(chats)
        }
    }

    internal suspend fun checkUpdates(): Response {
        return withContext(Dispatchers.IO) {
            chatClient.checkUpdates(AUTH_SCHEME.plus(token))
        }
    }

}