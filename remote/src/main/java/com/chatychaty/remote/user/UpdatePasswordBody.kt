package com.chatychaty.remote.user

class UpdatePasswordBody(
    private val currentPassword: String,
    private val newPassword: String,
)