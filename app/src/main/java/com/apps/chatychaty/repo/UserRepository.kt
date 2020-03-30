package com.apps.chatychaty.repo

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import com.apps.chatychaty.network.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

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

    suspend fun updatePhoto(token: String, mp: MultipartBody.Part) {
        withContext(Dispatchers.IO) {
            userClient.updatePhoto("Bearer $token", mp)
        }
    }

    suspend fun updateName(token: String, name: String) {
        withContext(Dispatchers.IO) {
            userClient.updateName("Bearer $token", name)
        }
    }
}