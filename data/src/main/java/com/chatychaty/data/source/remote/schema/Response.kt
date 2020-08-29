package com.chatychaty.data.source.remote.schema

data class Response<T>(

    val success: Boolean,

    val errors: List<String>? = null,

    val data: T? = null

)