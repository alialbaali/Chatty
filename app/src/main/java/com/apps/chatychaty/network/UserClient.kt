package com.apps.chatychaty.network

import com.apps.chatychaty.model.Response
import com.apps.chatychaty.model.User
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserClient {

    @POST("v2/Authentication/CreateAccount")
    suspend fun signUp(@Body user: User): Response

    @POST("v2/Authentication/Login")
    suspend fun signIn(@Body user: User): Response

    @Multipart
    @POST("v1/Profile/SetPhotoForSelf")
    suspend fun updatePhoto(@Header("Authorization") token: String, @Part img: MultipartBody.Part)

    @PATCH("v1/Profile/UpdateDisplayName")
    suspend fun updateName(@Header("Authorization") token: String, @Body name: String)
}