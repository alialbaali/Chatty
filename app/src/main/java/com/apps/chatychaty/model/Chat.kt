package com.apps.chatychaty.model

import com.squareup.moshi.Json

data class Chat(

    @Json(name = "Id")
    val id: Long = 0L,

    @Json(name = "FirstUserId")
    val senderId: Long,

    @Json(name = "SecondUserId")
    val receiverId: Long
)