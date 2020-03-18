package com.apps.chatychaty.network

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserClient {

    @Multipart
    @POST("v1/Authentication/CreateAccount")
    suspend fun createAccount(@Body user: User, @Part img: MultipartBody.Part): Response

    @POST("v1/Authentication/Login")
    suspend fun logIn(@Body user: User): Response


    @DELETE("v1/Main/DeleteAllMessages")
    suspend fun deleteAllMessages()

    @GET("")
    suspend fun getUsers(user: User): List<User>
}