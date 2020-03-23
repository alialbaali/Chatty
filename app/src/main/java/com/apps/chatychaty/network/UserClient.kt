package com.apps.chatychaty.network

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserClient {

    @POST("v2/Authentication/CreateAccount")
    suspend fun signUp(@Body user: User): Response

    @POST("v1/Authentication/Login")
    suspend fun signIn(@Body user: User): Response


    @DELETE("v1/Main/DeleteAllMessages")
    suspend fun deleteAllMessages()

    @GET("")
    suspend fun getUsers(user: User): List<User>
}