package com.apps.chatychaty.model

import com.squareup.moshi.Json

data class Message(

    @Json(name = "id")
    val id: Long = 0L,

    @Json(name = "body")
    var text: String = "",

    @Json(name = "sender")
    var senderId: Long,

    @Json(name = "delivered")
    val delivered: Boolean,

    @Json(name = "conversationId")
    val chatId: Long
)