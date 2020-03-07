package com.apps.chatychaty.model

import com.squareup.moshi.Json

data class Message(
    val id: Long = 0L,

    @Json(name = "body")
    var text: String,

    @Json(name = "sender")
    var user: String
)