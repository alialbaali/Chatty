package com.alialbaali.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.alialbaali.local.ChatDao
import com.alialbaali.model.Chat
import com.alialbaali.model.Response
import com.alialbaali.remote.ChatClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val chatClient: ChatClient, private val chatDao: ChatDao, private val sharedPreferences: SharedPreferences) {

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

    suspend fun getChatsClient() {
        withContext(Dispatchers.IO) {
            val chats = chatClient.getChats(AUTH_SCHEME.plus(sharedPreferences.getString(TOKEN, null)))
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
            chatClient.checkUpdates(AUTH_SCHEME.plus(sharedPreferences.getString(TOKEN, null)))
        }
    }
}