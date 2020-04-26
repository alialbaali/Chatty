package com.apps.chatychaty.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.chatychaty.model.Chat


@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertChat(chat: Chat)

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    fun getChat(chatId: Int): Chat

    @Query("SELECT * FROM chats ORDER BY chat_id DESC")
    fun getChats(): LiveData<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateChats(chats: List<Chat>)
}