package com.chatychaty.domain.model

data class User(
    val name: String,
    val username: String,
    val password: String?,
    val imageUrl: String?,
)