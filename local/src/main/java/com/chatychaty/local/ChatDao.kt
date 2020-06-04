package com.chatychaty.local

import androidx.room.*
import com.chatychaty.data.source.local.ChatLocalDataSource
import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.flow.Flow


@Dao
interface ChatDao : ChatLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override fun createChat(chat: Chat)

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    override fun getChatById(chatId: Int): Flow<Chat>

    @Query("SELECT * FROM chats ORDER BY chat_id DESC")
    override fun getChats(): Flow<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun updateChats(chats: List<Chat>)

    @Query("DELETE FROM chats")
    override fun deleteChats()
}