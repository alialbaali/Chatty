package com.chatychaty.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chatychaty.domain.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages WHERE chat_id = :chatId")
    fun getMessages(chatId: String): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE id = :messageId LIMIT 1")
    fun getMessageById(messageId: String): Flow<Message>

    @Query("SELECT * FROM messages WHERE is_new = 1")
    fun getNewMessages(): Flow<List<Message>>

    @Query(
        """
            SELECT *
            FROM messages
            WHERE sentDate
            IN (
                SELECT MAX(sentDate)
                FROM messages
                GROUP BY chat_id
            )
            """
    )
    fun getLastMessages(): Flow<List<Message>>

    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<Message>>

    @Insert
    suspend fun insertMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMessages(messages: List<Message>)

    @Query("UPDATE messages SET is_new = 0 WHERE chat_id = :chatId")
    suspend fun updateNewMessages(chatId: String)

    @Query("DELETE FROM messages")
    suspend fun deleteMessages()
}