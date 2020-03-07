package com.apps.chatychaty.model

import com.squareup.moshi.Json

data class User(
    val id: Long = 0L,

    @Json(name = "UserName")
    val username: String,
    val password: String
)