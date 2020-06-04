package com.chatychaty.remote

import com.chatychaty.data.source.remote.UserRemoteDataSource
import com.chatychaty.domain.model.Response
import com.chatychaty.domain.model.User
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

const val AUTH_HEADER = "Authorization"

interface UserClient : UserRemoteDataSource {

    @POST("v2/Authentication/CreateAccount")
    override suspend fun signUp(@Body user: User): Response

    @POST("v2/Authentication/Login")
    override suspend fun signIn(@Body user: User): Response

    @Multipart
    @POST("v1/Profile/SetPhotoForSelf")
    override suspend fun updatePhoto(@Header(AUTH_HEADER) token: String, @Part img: MultipartBody.Part) : String

    @PATCH("v1/Profile/UpdateDisplayName")
    override suspend fun updateName(@Header(AUTH_HEADER) token: String, @Body name: String)
}