package com.chatychaty.domain.model

import com.squareup.moshi.Json

data class Response(

    @Json(name = "success")
    val condition: Boolean = false,

    @Json(name = "token")
    val token: String? = null,

    @Json(name = "errors")
    val errors: List<String>? = null,

    @Json(name = "error")
    val error: String? = null,

    @Json(name = "profile")
    val user: User? = null,

    @Json(name = "chatId")
    val chatId: Int? = 0,

    @Json(name = "chatUpdate")
    val chatUpdate: Boolean = false,

    @Json(name = "messageUpdate")
    val messageUpdate: Boolean = false
)