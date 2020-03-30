package com.apps.chatychaty.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.chatychaty.model.Message

@Dao
interface MessageDao {

    @Insert
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages where chat_id = :chatId")
    fun getMessages(chatId: Int): LiveData<List<Message>>

    @Query("SELECT COUNT(*) FROM chats")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMessages(messages: List<Message>)

}