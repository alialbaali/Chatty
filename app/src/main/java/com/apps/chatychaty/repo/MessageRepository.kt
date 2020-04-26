package com.apps.chatychaty.repo

import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.apps.chatychaty.di.AUTH_SCHEME
import com.apps.chatychaty.local.MessageDao
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.remote.MessageClient
import com.apps.chatychaty.token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepository(private val messageClient: MessageClient, private val messageDao: MessageDao) {

    suspend fun insertMessageClient(token: String, message: Message) {
        withContext(Dispatchers.IO) {
            messageClient.insertMessage(AUTH_SCHEME.plus(token), message).let {
                messageDao.insertMessage(it)
            }
        }
    }

    suspend fun getMessagesClient(token: String) {
        withContext(Dispatchers.IO) {
            val messages = messageClient.getMessages(AUTH_SCHEME.plus(token), messageDao.count())
            messageDao.updateMessages(messages)
        }
    }

    suspend fun getMessagesDao(chatId: Int): LiveData<List<Message>> {
        return withContext(Dispatchers.Main) {
            messageDao.getMessages(chatId)
        }
    }

    suspend fun isMessageDelivered(messageId: Int) {
        withContext(Dispatchers.IO) {
            val value = messageClient.isMessageDelivered(AUTH_SCHEME.plus(token), messageId)
            if (value) {
                messageDao.updateDelivered()
            }
        }
    }

    suspend fun getLastMessage(chatId: Int): String {
        return withContext(Dispatchers.IO) {
            messageDao.getLastMessage(chatId)
        }
    }
}

