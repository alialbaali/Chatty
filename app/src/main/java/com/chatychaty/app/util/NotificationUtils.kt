package com.chatychaty.app.util

//import android.app.NotificationChannel
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.chatychaty.app.R

private const val CHANNEL_ID = "0"
private const val CHANNEL_NAME = "ChatyChaty"
private const val NOTIFICATION_ID = 0

fun NotificationManager.createNotification(context: Context, title: String, body: String) {

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(R.mipmap.ic_app)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.createChannel(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).also {
            it.enableVibration(true)
            it.enableLights(true)
            this.createNotificationChannel(it)
        }
    }
}