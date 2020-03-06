package com.apps.chatychaty.model

data class Message(
    val id: Long,
    var text: String,
    val user: User
)