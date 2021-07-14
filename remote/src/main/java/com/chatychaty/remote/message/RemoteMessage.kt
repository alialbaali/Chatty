package com.chatychaty.remote.message

data class RemoteMessage(
    val chatId: String,
    val messageId: String,
    val sender: String,
    val body: String,
    val delivered: Boolean?,
    val sentTime: String,
    val deliveryTime: String?,
)