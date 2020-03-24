package com.apps.chatychaty.network

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface UserClient {

    @POST("v2/Authentication/CreateAccount")
    suspend fun signUp(@Body user: User): Response

    @POST("v2/Authentication/Login")
    suspend fun signIn(@Body user: User): Response

    @DELETE("v1/Main/DeleteAllMessages")
    suspend fun deleteAllMessages()

    @GET("")
    suspend fun getUsers(user: User): List<User>
}