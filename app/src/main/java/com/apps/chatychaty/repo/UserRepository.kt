package com.apps.chatychaty.repo

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import com.apps.chatychaty.network.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userClient: UserClient) {

    suspend fun createAccount(user: User): Response {
        return withContext(Dispatchers.IO) {
            userClient.createAccount(user)
        }
    }

    suspend fun logIn(user: User): Response {
        return withContext(Dispatchers.IO) {
            userClient.createAccount(user)
        }
    }
}