package com.apps.chatychaty.model

import com.squareup.moshi.Json

data class User(
    val id: Long = 0L,

    @Json(name = "userName")
    var username: String = "",

    @Json(name = "password")
    var password: String = ""
)