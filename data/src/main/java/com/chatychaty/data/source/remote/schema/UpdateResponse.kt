package com.chatychaty.data.source.remote.schema

data class UpdateResponse(

    val chatUpdate: Boolean,

    val messageUpdate: Boolean,

    val deliverUpdate: Boolean

)