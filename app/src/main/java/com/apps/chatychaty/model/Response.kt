package com.apps.chatychaty.model

import com.squareup.moshi.Json

class Response(

    @Json(name = "success")
    val condition: Boolean,

    @Json(name = "token")
    val token: String?,

    @Json(name = "errors")
    val errors: String?
)