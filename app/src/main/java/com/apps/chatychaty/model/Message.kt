package com.apps.chatychaty.model

data class Message(
    val id: Long,
    val text: String,
    val user: User
)