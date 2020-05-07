package com.alialbaali.remote

import com.alialbaali.model.Response
import com.alialbaali.model.User
import okhttp3.MultipartBody
import retrofit2.http.*

const val AUTH_HEADER = "Authorization"

interface UserClient {

    @POST("v2/Authentication/CreateAccount")
    suspend fun signUp(@Body user: User): Response

    @POST("v2/Authentication/Login")
    suspend fun signIn(@Body user: User): Response

    @Multipart
    @POST("v1/Profile/SetPhotoForSelf")
    suspend fun updatePhoto(@Header(AUTH_HEADER) token: String, @Part img: MultipartBody.Part)

    @PATCH("v1/Profile/UpdateDisplayName")
    suspend fun updateName(@Header(AUTH_HEADER) token: String, @Body name: String)
}