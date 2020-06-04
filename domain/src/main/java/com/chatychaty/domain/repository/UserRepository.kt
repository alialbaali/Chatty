package com.chatychaty.domain.repository

import com.chatychaty.domain.model.User
import kotlinx.coroutines.flow.Flow

const val TOKEN = "token"
const val NAME = "name"
const val USERNAME = "username"
const val IMG_URL = "img_url"
const val AUTH_SCHEME = "Bearer "
const val IMG_NAME = "photoFile"
const val THEME = "theme"
const val SYSTEM_DEFAULT = "System default"
const val LIGHT_THEME = "Light theme"
const val DARK_THEME = "Dark theme"

interface UserRepository {

    val token: String?

    suspend fun signUp(user: User): Result<User>

    suspend fun signIn(user: User): Result<User>

    suspend fun updatePhoto(byteArray: ByteArray, fileName: String)

    suspend fun updateName(name: String)

    suspend fun signOut()

    suspend fun getUser(): Result<Flow<User>>

    suspend fun getThemeValue(): String

    suspend fun setThemeValue(value: String)

}