package com.chatychaty.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
    @ForeignKey(
        entity = Chat::class,
        parentColumns = ["chat_id"],
        childColumns = ["chat_id"],
        onDelete = ForeignKey.CASCADE
    )
    @Json(name = "chatId")
    val chatId: Int
)