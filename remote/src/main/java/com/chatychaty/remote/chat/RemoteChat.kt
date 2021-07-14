package com.chatychaty.remote.chat

import com.chatychaty.remote.user.RemoteUser

data class RemoteChat(
    val chatId: String,
    val profile: RemoteUser
)