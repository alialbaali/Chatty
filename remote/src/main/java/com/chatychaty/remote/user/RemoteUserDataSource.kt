package com.chatychaty.remote.user

import okhttp3.MultipartBody
import retrofit2.http.*

const val AUTH_HEADER = "Authorization"

interface RemoteUserDataSource {

    @POST("v1/Authentication/NewAccount")
    suspend fun signUp(@Body user: RemoteUser): TokenResponse

    @POST("v1/Authentication/Account")
    suspend fun signIn(@Body user: RemoteUser): TokenResponse

    @PATCH("v1/Authentication/Password")
    suspend fun updatePassword(@Header(AUTH_HEADER) token: String, @Body updatePasswordBody: UpdatePasswordBody)

    @PATCH("v1/Profile/DisplayName")
    suspend fun updateName(@Header(AUTH_HEADER) token: String, @Body name: String): String

    @Multipart
    @POST("v1/Profile/Photo")
    suspend fun updateImage(@Header(AUTH_HEADER) token: String, @Part image: MultipartBody.Part): String

    @POST("v1/Profile/Feedback")
    suspend fun submitFeedback(@Header(AUTH_HEADER) token: String, @Body stars: Int, @Body feedback: String)

    @DELETE("v1/Profile")
    suspend fun deleteAccount(@Header(AUTH_HEADER) token: String)

}