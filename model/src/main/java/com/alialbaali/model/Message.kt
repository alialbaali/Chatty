package com.alialbaali.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "messages")
data class Message(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "message_id")
    @Json(name = "messageId")
    val messageId: Int = 0,

    @ColumnInfo(name = "message_body")
    @Json(name = "body")
    var messageBody: String = "",

    @ColumnInfo(name = "name")
    @Json(name = "sender")
    var username: String = "",

    @ColumnInfo(name = "delivered")
    @Json(name = "delivered")
    var delivered: Boolean? = null,

    @ColumnInfo(name = "chat_id")
    @Json(name = "chatId")
    val chatId: Int
)