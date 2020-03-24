package com.apps.chatychaty.repo

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import com.apps.chatychaty.network.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userClient: UserClient) {

    suspend fun signUp(user: User): Response {
        return withContext(Dispatchers.IO) {
            userClient.signUp(user)
        }
    }

    suspend fun signIn(user: User): Response {
        return withContext(Dispatchers.IO) {
            userClient.signIn(user)
        }
    }

    suspend fun deleteAllMessages() {
        withContext(Dispatchers.IO) {
            userClient.deleteAllMessages()
        }
    }

    suspend fun getUsers(user: User): List<User> {
        return withContext(Dispatchers.IO) {
            userClient.getUsers(user)
        }
    }
}