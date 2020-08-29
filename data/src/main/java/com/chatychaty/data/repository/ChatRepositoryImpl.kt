package com.chatychaty.data.repository

import com.chatychaty.data.retrieveErrors
import com.chatychaty.data.source.local.ChatLocalDataSource
import com.chatychaty.data.source.local.UserLocalDataSource
import com.chatychaty.data.source.remote.ChatRemoteDataSource
import com.chatychaty.data.tryCatching
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.repository.AUTH_SCHEME
import com.chatychaty.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class
ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatRepository {

    override val token: String
        get() = AUTH_SCHEME.plus(userLocalDataSource.getUserToken())

    override suspend fun createChat(username: String): Result<Chat> = withContext(Dispatchers.IO) {

        tryCatching {
            chatRemoteDataSource.createChat(token, username)
        }.mapCatching { response ->

            if (response.success) {
                val chat = response.data!!
                chatLocalDataSource.createChat(chat)
                Result.success(chat)
            } else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse {
            Result.failure(it)
        }

    }

    override suspend fun getChats(): Result<Flow<List<Chat>>> = Result.success(chatLocalDataSource.getChats())

    override suspend fun getRemoteChats() = withContext(Dispatchers.IO) {

        tryCatching {
            chatRemoteDataSource.getChats(token)
        }.mapCatching { response ->

            if (response.success) {
                val chats = response.data!!
                chatLocalDataSource.updateChats(chats)
                Result.success(chats)
            } else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse {
            Result.failure(it)
        }

    }

    override suspend fun checkUpdates(): Result<Triple<Boolean, Boolean, Boolean>> = withContext(Dispatchers.IO) {

        tryCatching {
            chatRemoteDataSource.checkUpdates(token)
        }.mapCatching { response ->

            if (response.success) {
                val updateResponse = response.data!!
                Result.success(Triple(updateResponse.chatUpdate, updateResponse.messageUpdate, updateResponse.deliverUpdate))
            } else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse {
            Result.failure(it)
        }

    }

    override suspend fun deleteChats() = chatLocalDataSource.deleteChats()

    override fun getChatById(chatId: Int): Result<Flow<Chat>> = Result.success(chatLocalDataSource.getChatById(chatId))

}