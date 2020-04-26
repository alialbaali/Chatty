package com.apps.chatychaty.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("UPDATE messages SET delivered = 1 ")
    suspend fun updateDelivered()

    @Query("SELECT message_body FROM messages WHERE chat_id = :chatId ORDER BY message_id DESC LIMIT 1")
    fun getLastMessage(chatId: Int): String
}