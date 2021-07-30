package com.chatychaty.app.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.chatychaty.app.R
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message

private const val CHANNEL_ID = "0"
private const val CHANNEL_NAME = "ChatyChaty"
private const val NOTIFICATION_ID = 0
const val REMOTE_INPUT_KEY = "Body"

fun NotificationManager.createNotification(context: Context, chat: Chat, messages: List<Message>, isSender: Boolean) {
    if (!chat.isMuted) {

        val pendingIntent = context.createNotificationPendingIntent(chat.chatId)

        val sender = Person.Builder()
            .setName("You")
            .build()

        val receiver = Person.Builder()
            .setName(chat.name)
            .build()

        val style = NotificationCompat.MessagingStyle(sender).also { style ->
            messages.forEach { message ->
                style.addMessage(message.body, message.sentDate.toEpochMilliseconds(), if (isSender) sender else receiver)
            }
        }

        val markAsReadAction = context.createMarkAsReadAction(chat.chatId)

        val replyAction = context.createReplyAction(chat.chatId, messages.first().id)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_app_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(style)
            .addAction(markAsReadAction)
            .addAction(replyAction)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .build()

        notify(chat.chatId, NOTIFICATION_ID, notification)
    }
}

private fun Context.createNotificationPendingIntent(chatId: String): PendingIntent {
    return NavDeepLinkBuilder(this)
        .setGraph(R.navigation.navigation)
        .setDestination(R.id.messageListFragment)
        .setArguments(bundleOf("chatId" to chatId))
        .createPendingIntent()
}

fun NotificationManager.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            enableVibration(true)
            enableLights(true)
        }
        createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.cancelNotification(chatId: String) = cancel(chatId, NOTIFICATION_ID)
