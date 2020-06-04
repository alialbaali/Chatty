package com.chatychaty.data.repository

import com.chatychaty.data.source.local.MessageLocalDataSource
import com.chatychaty.data.source.local.UserLocalDataSource
import com.chatychaty.data.source.remote.MessageRemoteDataSource
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.AUTH_SCHEME
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MessageRepositoryImpl(
    private val remoteSource: MessageRemoteDataSource,
    private val localSource: MessageLocalDataSource,
    private val userSharedPreferences: UserLocalDataSource
) : MessageRepository {

    override val token: String
        get() = AUTH_SCHEME.plus(userSharedPreferences.getUserToken())


    override suspend fun createMessage(message: Message) {
        message.also {
            it.messageBody = it.messageBody.trim()
        }
        withContext(Dispatchers.IO) {
            remoteSource.createMessage(token, message).let {
                localSource.createMessage(it)
            }
        }
    }

    override suspend fun getRemoteMessages() {
        withContext(Dispatchers.IO) {
            val lastMessageId = localSource.count()
            remoteSource.getMessages(token, lastMessageId).also {
                localSource.updateMessages(it)
            }
        }
    }

    override suspend fun getMessages(chatId: Int): Result<Flow<List<Message>>> {
        return Result.success(localSource.getMessages(chatId))
    }

//    override suspend fun getMessages(chatId: Int): Result<List<Message>?> {
//        return withContext(Dispatchers.IO) {
//            if (chatId == 0) {
//                try {
//                    val messages = remoteSource.getMessages(AUTH_SCHEME.plus(token), localSource.count())
//                    localSource.updateMessages(messages)
//                    Result.success(null)
//                } catch (e: Exception) {
//                    Result.failure<List<Message>>(e)
//                }
//            } else {
//                Result.success(localSource.getMessages(chatId))
//            }
//        }
//    }

    override suspend fun isMessageDelivered(messageId: Int) {
        withContext(Dispatchers.IO) {
            val value = remoteSource.isMessageDelivered(token, messageId)
            if (value) {
                localSource.updateDelivered()
            }
        }
    }

    override suspend fun getLastMessage(chatId: Int): Result<Flow<String>> {
            return Result.success(localSource.getLastMessage(chatId))
    }

    override suspend fun deleteMessages() = localSource.deleteMessages()
}

