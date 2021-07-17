package com.chatychaty.app.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.chatychaty.app.R

fun Context.createMarkAsReadAction(chatId: String): NotificationCompat.Action {
    val intent = Intent(this, NotificationMarkAsReadReceiver::class.java)
        .putExtra("chatId", chatId)

    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    return NotificationCompat.Action.Builder(null, getString(R.string.mark_as_read), pendingIntent)
        .build()
}

fun Context.createReplyAction(chatId: String, messageId: String): NotificationCompat.Action {

    val remoteInput = RemoteInput.Builder(REMOTE_INPUT_KEY)
        .setLabel(getString(R.string.type_a_message))
        .build()

    val intent = Intent(this, NotificationReplyReceiver::class.java)
        .putExtra("chatId", chatId)
        .putExtra("messageId", messageId)

    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    return NotificationCompat.Action.Builder(R.drawable.ic_round_edit_24, getString(R.string.reply), pendingIntent)
        .addRemoteInput(remoteInput)
        .build()
}