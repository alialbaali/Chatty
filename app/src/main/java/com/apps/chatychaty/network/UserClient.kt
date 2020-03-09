package com.apps.chatychaty.network

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface UserClient {

    @POST("v1/Authentication/CreateAccount")
    suspend fun createAccount(@Body user: User): Response

    @POST("v1/Authentication/Login")
    suspend fun logIn(@Body user: User): Response

}