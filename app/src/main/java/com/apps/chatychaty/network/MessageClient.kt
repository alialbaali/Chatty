package com.apps.chatychaty.network

import com.apps.chatychaty.model.Message
import retrofit2.http.*

interface MessageClient {

    @POST("v2/Main/PostMessage")
    suspend fun postMessage(@Body message: Message, @Header("Authorization") value: String)

    @GET("v1/Main/GetAllMessages")
    suspend fun getMessages(): List<Message>

    @GET("v1/Main/GetImg")
    suspend fun getImg()
    // TODO (return an image object)

    @GET("v1/Main/GetNewMessages")
    suspend fun getNewMessages(@Query("id") id: Int): List<Message>
}