package com.chatychaty.app.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.chatychaty.app.R
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message

private const val CHANNEL_ID = "0"
private const val CHANNEL_NAME = "ChatyChaty"
private const val NOTIFICATION_ID = 0
const val REMOTE_INPUT_KEY = "Body"

fun Context.createNotification() {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

fun NotificationManager.createNotification(context: Context, chat: Chat, messages: List<Message>, isSender: Boolean) {
    if (!chat.isMuted) {
        val pendingIntent = context.createNotificationPendingIntent(chat.chatId)

        val remoteInput = RemoteInput.Builder(REMOTE_INPUT_KEY)
            .setLabel(context.getString(R.string.type_a_message))
            .build()

        val replyIntent = Intent(context, NotificationReceiver::class.java)
            .putExtra("chatId", chat.chatId)
            .putExtra("messageId", messages.first().id)

        val replyPendingIntent = PendingIntent.getBroadcast(context, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

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

        val action = NotificationCompat.Action.Builder(R.drawable.ic_round_edit_24, context.getString(R.string.reply), replyPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(style)
            .addAction(action)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setAutoCancel(true)

        notify(chat.chatId, NOTIFICATION_ID, builder.build())
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
