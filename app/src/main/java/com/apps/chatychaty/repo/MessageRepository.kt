package com.apps.chatychaty.repo

import androidx.lifecycle.LiveData
import com.apps.chatychaty.database.MessageDao
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.network.AUTH_SCHEME
import com.apps.chatychaty.network.MessageClient
import com.apps.chatychaty.token
import kotlinx.coroutines.*

class MessageRepository(
    private val messageClient: MessageClient,
    private val messageDao: MessageDao
) {

    suspend fun insertMessageClient(token: String, message: Message): Message {
        return withContext(Dispatchers.IO) {
            messageClient.insertMessage(AUTH_SCHEME.plus(token), message)
        }
    }

    suspend fun insertMessageDao(message: Message) {
        withContext(Dispatchers.IO) {
            messageDao.insertMessage(message)
        }
    }

    suspend fun getMessagesClient(token: String, lastMessageId: Int): List<Message> {
        return withContext(Dispatchers.IO) {
            messageClient.getMessages(AUTH_SCHEME.plus(token), lastMessageId)
        }
    }

    suspend fun getMessagesDao(chatId: Int): LiveData<List<Message>> {
        return withContext(Dispatchers.Main) {
            messageDao.getMessages(chatId)
        }
    }

    suspend fun countDao(): Int {
        return withContext(Dispatchers.IO) {
            messageDao.count()
        }
    }

    suspend fun updateMessagesDao(messages: List<Message>) {
        withContext(Dispatchers.IO) {
            messageDao.updateMessages(messages)
        }
    }

    suspend fun isMessageDelivered(messageId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            messageClient.isMessageDelivered(AUTH_SCHEME.plus(token), messageId)
        }
    }

    suspend fun updateDelivered() {
        withContext(Dispatchers.IO) {
            messageDao.updateDelivered()
        }
    }

    suspend fun getLastMessage(chatId: Int): String {
        return withContext(Dispatchers.IO) {
            messageDao.getLastMessage(chatId)
        }
    }
}

