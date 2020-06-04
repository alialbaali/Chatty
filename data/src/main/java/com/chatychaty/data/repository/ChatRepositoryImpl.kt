package com.chatychaty.data.repository

import android.content.SharedPreferences
import androidx.lifecycle.asLiveData
import com.chatychaty.data.source.local.ChatLocalDataSource
import com.chatychaty.data.source.local.UserLocalDataSource
import com.chatychaty.data.source.remote.ChatRemoteDataSource
import com.chatychaty.data.util.ExceptionHandler
import com.chatychaty.data.util.tryCatching
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.repository.AUTH_SCHEME
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.TOKEN
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class
ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ChatRepository{

    override val token: String
        get() = AUTH_SCHEME.plus(userLocalDataSource.getUserToken())


    override suspend fun createChat(username: String): Result<Chat> {
        return withContext(Dispatchers.IO) {

            val response = chatRemoteDataSource.createChat(token, username)

            if (response.condition) {
                val chat = Chat(response.chatId!!, response.user!!)

                chatLocalDataSource.createChat(chat)

                Result.success(chat)

            } else {
                Result.failure(Throwable(response.error))
            }
        }
    }

    override suspend fun getChats(): Result<Flow<List<Chat>>> {
//        return withContext(Dispatchers.IO) {
//            getRemoteChats()
        return tryCatching {
            chatLocalDataSource.getChats()
        }
//           return chatLocalDataSource.getChats()
//        }
    }

    override suspend fun getRemoteChats() {
        withContext(Dispatchers.IO) {
            val chats = chatRemoteDataSource.getChats(token)
            chatLocalDataSource.updateChats(chats)
        }
    }

    override suspend fun checkUpdates(): Pair<Boolean, Boolean> {
        return withContext(Dispatchers.IO) {
            val response = chatRemoteDataSource.checkUpdates(token)
            Pair(response.chatUpdate, response.messageUpdate)
        }
    }

    override suspend fun deleteChats() = chatLocalDataSource.deleteChats()

    override fun getChatById(chatId: Int): Result<Flow<Chat>> {
       return Result.success(chatLocalDataSource.getChatById(chatId))
    }
}