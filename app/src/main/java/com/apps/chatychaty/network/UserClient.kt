package com.apps.chatychaty.network

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import retrofit2.http.POST

interface UserClient {

    @POST("/Authentication/CreateAccount")
    suspend fun createAccount(user: User): Response

    @POST("/Authentication/Login")
    suspend fun logIn(user: User): Response

}