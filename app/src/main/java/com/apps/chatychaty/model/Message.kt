package com.apps.chatychaty.model

data class Message(
    val id: Long = 0L,
    var text: String,
    var user: String
)