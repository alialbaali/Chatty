package com.chatychaty.data.source.remote

import com.chatychaty.data.source.remote.schema.Response
import com.chatychaty.data.source.remote.schema.UpdateResponse
import com.chatychaty.domain.model.Chat

interface ChatRemoteDataSource {

    suspend fun createChat(token: String, username: String): Response<Chat>

    suspend fun getChats(token: String): Response<List<Chat>>

    suspend fun checkUpdates(token: String): Response<UpdateResponse>
}