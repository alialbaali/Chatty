package com.apps.chatychaty.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.apps.chatychaty.model.Chat


@Dao
interface ChatDao {

    @Insert
    fun insertChat(chat: Chat)

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    fun getChat(chatId: Long): Chat

    @Query("SELECT * FROM chats")
    fun getChats(): LiveData<List<Chat>>
}