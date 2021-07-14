package com.chatychaty.remote.user

import com.squareup.moshi.Json

data class TokenResponse(
    val token: String,

    @Json(name = "profile")
    val user: RemoteUser,
)