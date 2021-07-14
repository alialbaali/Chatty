package com.chatychaty.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.flow.Flow


@Dao
interface ChatDao {

    @Query("SELECT * FROM chats WHERE is_archived = 0")
    fun getChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE is_archived = 1")
    fun getArchivedChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    fun getChatById(chatId: String): Flow<Chat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateChats(chats: List<Chat>)

    @Query("UPDATE chats SET is_archived = 1 WHERE chat_id = :chatId")
    suspend fun archiveChat(chatId: String)

    @Query("UPDATE chats SET is_archived = 0 WHERE chat_id = :chatId")
    suspend fun unarchiveChat(chatId: String)

    @Query("UPDATE chats SET is_pinned = 1 WHERE chat_id = :chatId")
    suspend fun pinChat(chatId: String)

    @Query("UPDATE chats SET is_pinned = 0 WHERE chat_id = :chatId")
    suspend fun unpinChat(chatId: String)

    @Query("UPDATE chats SET is_muted = 1 WHERE chat_id = :chatId")
    suspend fun muteChat(chatId: String)

    @Query("UPDATE chats SET is_muted = 0 WHERE chat_id = :chatId")
    suspend fun unmuteChat(chatId: String)

    @Query("DELETE FROM chats")
    suspend fun deleteChats()
}