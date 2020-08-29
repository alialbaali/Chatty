package com.chatychaty.data.source.remote

import com.chatychaty.data.source.remote.schema.Response
import com.chatychaty.data.source.remote.schema.user.UserResponse
import com.chatychaty.domain.model.User
import okhttp3.MultipartBody

interface UserRemoteDataSource {

    suspend fun signUp(user: User): Response<UserResponse>

    suspend fun signIn(user: User): Response<UserResponse>

    suspend fun changePassword(currentPassword: String, newPassword: String): Response<Unit>

    suspend fun updateName(token: String, name: String): Response<String>

    suspend fun updateImage(token: String, image: MultipartBody.Part): Response<String>

    suspend fun submitFeedback(token: String, stars: Int, feedback: String): Response<Unit>

    suspend fun deleteAccount(token: String): Response<Unit>

}