package com.apps.chatychaty.model

import com.squareup.moshi.Json

data class User(
    val id: Long = 0L,

    @Json(name = "displayName")
    var name: String = "",

    @Json(name = "userName")
    var username: String = "",

    @Json(name = "password")
    var password: String = "",

    @Json(name = "img_url")
    var imgUrl: String = ""
)