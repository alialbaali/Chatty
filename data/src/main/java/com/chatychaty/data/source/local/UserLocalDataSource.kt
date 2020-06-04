package com.chatychaty.data.source.local

import com.chatychaty.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    fun getUserToken(): String

    suspend fun createUser(user: User, token: String)

    suspend fun setUserValue(key: String, value: String)

    suspend fun deleteUser()

    suspend fun getThemeValue(): String

    suspend fun setThemeValue(value: String)

    fun getUser(): Flow<User>
}
