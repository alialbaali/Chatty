package com.chatychaty.domain.repository

import com.chatychaty.domain.model.User
import kotlinx.coroutines.flow.Flow

const val TOKEN = "token"
const val NAME = "name"
const val USERNAME = "username"
const val IMAGE_URL = "image_url"
const val AUTH_SCHEME = "Bearer "
const val IMAGE_NAME = "photoFile"
const val THEME = "theme"
const val SYSTEM_DEFAULT = "System default"
const val LIGHT_THEME = "Light theme"
const val DARK_THEME = "Dark theme"

interface UserRepository {

    val token: String

    suspend fun signUp(user: User): Result<User>

    suspend fun signIn(user: User): Result<User>

    suspend fun updateImage(byteArray: ByteArray, fileName: String) : Result<Unit>

    suspend fun updateName(name: String): Result<Unit>

    suspend fun signOut()

    suspend fun getUser(): Result<Flow<User>>

    suspend fun getTheme(): String

    suspend fun setTheme(value: String)

    suspend fun submitFeedback(stars: Int, feedback: String): Result<Unit>

    suspend fun deleteAccount(): Result<Unit>

}