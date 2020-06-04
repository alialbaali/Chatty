package com.chatychaty.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chatychaty.data.source.local.MessageLocalDataSource
import com.chatychaty.domain.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao : MessageLocalDataSource {

    @Insert
    override suspend fun createMessage(message: Message)

    @Query("SELECT * FROM messages WHERE chat_id = :chatId")
    override fun getMessages(chatId: Int): Flow<List<Message>>

    @Query("SELECT COUNT(*) FROM chats")
    override suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun updateMessages(messages: List<Message>)

    @Query("UPDATE messages SET delivered = 1 ")
    override suspend fun updateDelivered()

    @Query("SELECT message_body FROM messages WHERE chat_id = :chatId ORDER BY message_id DESC LIMIT 1")
    override fun getLastMessage(chatId: Int): Flow<String>

    @Query("DELETE FROM messages")
    override suspend fun deleteMessages()
}