package com.chatychaty.data.source.remote.schema.user

import com.chatychaty.domain.model.User
import com.squareup.moshi.Json

data class UserResponse(

    @Json(name = "token")
    val token: String,

    @Json(name = "profile")
    val user: User

)