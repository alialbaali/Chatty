package com.chatychaty.remote.user

data class RemoteUser(
    val displayName: String,
    val username: String,
    val password: String?,
    val photoURL: String?,
)
