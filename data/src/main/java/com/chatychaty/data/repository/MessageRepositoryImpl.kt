package com.chatychaty.data.repository

import com.chatychaty.data.retrieveErrors
import com.chatychaty.data.source.local.MessageLocalDataSource
import com.chatychaty.data.source.local.UserLocalDataSource
import com.chatychaty.data.source.remote.MessageRemoteDataSource
import com.chatychaty.data.tryCatching
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.AUTH_SCHEME
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MessageRepositoryImpl(
    private val remoteSource: MessageRemoteDataSource,
    private val localSource: MessageLocalDataSource,
    private val userSharedPreferences: UserLocalDataSource
) : MessageRepository {

    override val token: String
        get() = AUTH_SCHEME.plus(userSharedPreferences.getUserToken())


    override suspend fun createMessage(message: Message) = withContext(Dispatchers.IO) {

        tryCatching {
            remoteSource.createMessage(token, message.apply { messageBody = messageBody.trim() })
        }.mapCatching { response ->

            if (response.success) {
                val data = response.data!!
                localSource.createMessage(message)
                Result.success(data)
            } else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse {
            Result.failure(it)
        }

    }

    override suspend fun getRemoteMessages() = withContext(Dispatchers.IO) {

        val lastMessageId = localSource.count()

        tryCatching {
            remoteSource.getMessages(token, lastMessageId)
        }.mapCatching { response ->

            if (response.success) {
                val messages = response.data!!
                localSource.updateMessages(messages)
                Result.success(messages)
            } else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse {
            Result.failure(it)
        }

    }


    override suspend fun getMessages(chatId: Int) = localSource.getMessages(chatId).map { messages -> Result.success(messages) }

    override suspend fun isMessageDelivered(messageId: Int) = withContext(Dispatchers.IO) {

        tryCatching {
            remoteSource.isMessageDelivered(token, messageId)
        }.mapCatching { response ->

            if (response.success) {

                if (response.data!!) localSource.updateDelivered()

                Result.success(Unit)

            } else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse {
            Result.failure(it)
        }

    }

    override suspend fun getLastMessage(chatId: Int): Result<Flow<String>> = Result.success(localSource.getLastMessage(chatId))

    override suspend fun deleteMessages() = localSource.deleteMessages()

}

