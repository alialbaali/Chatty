package com.apps.chatychaty.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.apps.chatychaty.model.Message

@Dao
interface MessageDao {

    @Insert
    fun insertMessage(message: Message)

    @Query("SELECT * FROM messages where chat_id = :chatId")
    fun getMessages(chatId: Long): LiveData<List<Message>>

}