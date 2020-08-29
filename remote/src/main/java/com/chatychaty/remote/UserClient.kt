package com.chatychaty.remote

import com.chatychaty.data.source.remote.UserRemoteDataSource
import com.chatychaty.data.source.remote.schema.Response
import com.chatychaty.data.source.remote.schema.user.UserResponse
import com.chatychaty.domain.model.User
import okhttp3.MultipartBody
import retrofit2.http.*

const val AUTH_HEADER = "Authorization"

interface UserClient : UserRemoteDataSource {

    @POST("v3/Authentication/NewAccount")
    override suspend fun signUp(@Body user: User): Response<UserResponse>

    @POST("v3/Authentication/Account")
    override suspend fun signIn(@Body user: User): Response<UserResponse>

    @POST("v3/Authentication/Password")
    override suspend fun changePassword(@Body currentPassword: String, @Body newPassword: String): Response<Unit>

    @PATCH("v3/Profile/DisplayName")
    override suspend fun updateName(@Header(AUTH_HEADER) token: String, @Body name: String): Response<String>

    @Multipart
    @POST("v3/Profile/Photo")
    override suspend fun updateImage(@Header(AUTH_HEADER) token: String, @Part image: MultipartBody.Part): Response<String>

    @POST("v3/Profile/Feedback")
    override suspend fun submitFeedback(@Header(AUTH_HEADER) token: String, @Body stars: Int, @Body feedback: String): Response<Unit>

    @DELETE("v3/Profile")
    override suspend fun deleteAccount(@Header(AUTH_HEADER) token: String): Response<Unit>

}