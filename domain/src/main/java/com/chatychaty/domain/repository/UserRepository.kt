package com.chatychaty.domain.repository

import com.chatychaty.domain.model.Theme
import com.chatychaty.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val dispatcher: CoroutineDispatcher

    suspend fun signUp(user: User): Result<User>

    suspend fun signIn(user: User): Result<User>

    suspend fun updateImage(byteArray: ByteArray, fileName: String): Result<Unit>

    suspend fun updateName(name: String): Result<Unit>

    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit>

    suspend fun signOut()

    fun getCurrentUser(): Flow<User?>

    fun getTheme(): Flow<Theme>

    suspend fun updateTheme(value: Theme)

    suspend fun submitFeedback(stars: Int, feedback: String): Result<Unit>

    suspend fun deleteAccount(): Result<Unit>

}