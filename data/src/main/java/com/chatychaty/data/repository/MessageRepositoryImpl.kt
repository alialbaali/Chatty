package com.chatychaty.data.repository

import com.chatychaty.data.asBearerToken
import com.chatychaty.data.request
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.MessageRepository
import com.chatychaty.local.MessageDao
import com.chatychaty.local.UserDao
import com.chatychaty.remote.message.NewMessageBody
import com.chatychaty.remote.message.RemoteMessage
import com.chatychaty.remote.message.RemoteMessageDataSource
import com.chatychaty.remote.message.SignalRHub
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.toInstant

class MessageRepositoryImpl(
    private val remoteDataSource: RemoteMessageDataSource,
    private val messageDao: MessageDao,
    userDao: UserDao,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val signalRHub: SignalRHub,
) : MessageRepository {

    private val token: Flow<String> = userDao.getUserToken()

    override suspend fun connectHub() = withContext(dispatcher) {
        signalRHub.connect(token.first())
    }

    override fun getMessages(chatId: String): Flow<List<Message>> = messageDao.getMessages(chatId)

    override fun getMessageById(messageId: String): Flow<Message> = messageDao.getMessageById(messageId)

    override suspend fun createMessage(message: Message): Result<Message> = request(dispatcher) {
        val newMessageBody = NewMessageBody(message.chatId, message.body)
        remoteDataSource.createMessage(token.asBearerToken(), newMessageBody)
    }.mapCatching { it.toDomainMessage() }
        .onSuccess {
            withContext(dispatcher) {
                messageDao.insertMessage(it)
            }
        }

    override fun getNewMessages(): Flow<List<Message>> = messageDao.getNewMessages()

    override fun getLastMessages(): Flow<List<Message>> = messageDao.getLastMessages()

    override suspend fun isMessageDelivered(messageId: String): Result<Boolean> = request(dispatcher) {
        remoteDataSource.isMessageDelivered(token.asBearerToken(), messageId)
    }

    override suspend fun refreshMessages() {
        withContext(dispatcher) {
            val lastMessage = messageDao.getAllMessages()
                .map { it.maxByOrNull { message -> message.sentDate } }
                .first()

            request(dispatcher) {
                remoteDataSource
                    .getMessages(token.asBearerToken(), lastMessage?.sentDate?.toString())
                    .map { remoteMessage ->
                        val isMessageInDB = messageDao.getAllMessages()
                            .first()
                            .any { it.id == remoteMessage.messageId }
                        remoteMessage.toDomainMessage(isNew = !isMessageInDB)
                    }
                    .also { messageDao.updateMessages(it) }
            }

            request(dispatcher) {
                remoteDataSource.getMessageStatus(token.asBearerToken(), lastMessage?.sentDate?.toString())
                    .map { it.toDomainMessage() }
                    .also { messageDao.updateMessages(it) }
            }

        }
    }

    override suspend fun syncMessages() = withContext(dispatcher) {
        signalRHub.collectMessages { messages ->
            messages
                .map { it.toDomainMessage(isNew = true) }
                .also { domainMessages ->
                    runBlocking(dispatcher) {
                        messageDao.updateMessages(domainMessages)
                    }
                }
        }
        signalRHub.collectMessageStatus { message ->
            runBlocking(dispatcher) {
                messageDao.updateMessages(listOf(message.toDomainMessage()))
            }
        }
    }

    override suspend fun updateNewMessages() = messageDao.updateNewMessages()

    override suspend fun deleteMessages() = withContext(dispatcher) {
        messageDao.deleteMessages()
    }

    override suspend fun disconnectHub() = signalRHub.disconnect()

    private fun RemoteMessage.toDomainMessage(isNew: Boolean = false): Message {
        return Message(
            messageId,
            chatId,
            body,
            sender,
            sentTime.isNotBlank(),
            !deliveryTime.isNullOrBlank(),
            sentTime.toInstant(),
            isNew,
        )
    }

}