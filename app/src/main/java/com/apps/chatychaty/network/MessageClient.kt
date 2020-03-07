package com.apps.chatychaty.network

import com.apps.chatychaty.model.Message
import retrofit2.http.GET
import retrofit2.http.POST

interface MessageClient {

    @POST("/Main/Get/PostMessage")
    suspend fun postMessage(message: Message)

    @GET("/Main/GetAllMessages")
    suspend fun getMessages(): List<Message>

    @GET("/Main/GetImg")
    suspend fun getImg()
    // TODO (return an image object)
}