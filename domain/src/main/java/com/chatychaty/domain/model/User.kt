package com.chatychaty.domain.model

import com.squareup.moshi.Json

data class User(

    @Json(name = "displayName")
    var name: String = "",

    @Json(name = "username")
    var username: String = "",

    @Json(name = "password")
    var password: String = "",

    @Json(name = "photoURL")
    var imgUrl: String? = ""
)