package com.chatychaty.app.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationMarkAsReadReceiver : BroadcastReceiver(), KoinComponent {

    private val messageRepository by inject<MessageRepository>()

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val chatId = intent?.getStringExtra("chatId")!!
        notificationManager.cancelNotification(chatId)
        runBlocking(Dispatchers.IO) {
            messageRepository.updateNewMessages(chatId)
        }
    }

}