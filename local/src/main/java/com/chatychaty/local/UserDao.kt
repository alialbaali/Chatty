package com.chatychaty.local

import com.chatychaty.domain.model.Theme
import com.chatychaty.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserDao {

    val isUserSignedIn: Flow<Boolean>

    fun getUser(): Flow<User?>

    suspend fun createOrUpdateUser(user: User, token: String?)

    fun getUserToken(): Flow<String>

    fun getTheme(): Flow<Theme>

    suspend fun updateTheme(value: Theme)

    suspend fun deleteUser()

}