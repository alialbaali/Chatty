package com.chatychaty.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "chats")
data class Chat(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "chat_id")
    @Json(name = "chatId")
    val chatId: Int,

    @Embedded
    @Json(name = "profile")
    val user: User
)