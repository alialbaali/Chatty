package com.apps.chatychaty.repo

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import com.apps.chatychaty.network.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class UserRepository(private val userClient: UserClient) {

    suspend fun createAccount(user: User, mp: MultipartBody.Part): Response {
        return withContext(Dispatchers.IO) {
            userClient.createAccount(user, mp)
        }
    }

    suspend fun logIn(user: User): Response {
        return withContext(Dispatchers.IO) {
            userClient.logIn(user)
        }
    }

    suspend fun deleteAllMessages(){
        withContext(Dispatchers.IO){
            userClient.deleteAllMessages()
        }
    }
}