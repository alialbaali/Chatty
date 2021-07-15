package com.chatychaty.app.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val messageRepository by inject<MessageRepository>()
    private val chatRepository by inject<ChatRepository>()

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val bundle = RemoteInput.getResultsFromIntent(intent)
        val chatId = intent?.getStringExtra("chatId")!!
        val messageId = intent.getStringExtra("messageId")!!
        val text = bundle.getCharSequence(REMOTE_INPUT_KEY).toString()
        val message = Message(id = messageId, chatId = chatId, body = text, username = "")
        runBlocking(Dispatchers.IO) {
            val chat = chatRepository.getChatById(chatId)
                .firstOrNull()
            chat?.let {
                notificationManager.createNotification(context, it, listOf(message), true)
            }
            messageRepository.createMessage(message)
        }
    }

}