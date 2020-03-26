package com.apps.chatychaty.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chats")
data class Chat(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "chat_id")
    val chatId: Int,

    @Embedded
    val user: User
)