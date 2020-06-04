package com.chatychaty.data.source.remote

import com.chatychaty.domain.model.Response
import com.chatychaty.domain.model.User
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody

interface UserRemoteDataSource {

    suspend fun signUp(user: User): Response

    suspend fun signIn(user: User): Response

    suspend fun updatePhoto(token: String, img: MultipartBody.Part) : String

    suspend fun updateName(token: String, name: String)
}