package com.apps.chatychaty.repo

import com.apps.chatychaty.model.Message
import com.apps.chatychaty.network.MessageClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MessageRepository(private val messageClient: MessageClient) {

    suspend fun postMessage(message: Message, value: String) {
        withContext(Dispatchers.IO) {
            messageClient.postMessage(message,"Bearer $value")
        }
    }

    suspend fun getMessages(): List<Message> {
        return withContext(Dispatchers.IO) {
            messageClient.getMessages()
        }
    }

    suspend fun getImg() {
        withContext(Dispatchers.IO) {
            messageClient.getImg()
        }
        TODO("make the function return an image type")
    }
}

