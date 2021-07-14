package com.chatychaty.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "chat_id")
    val chatId: String,

    val body: String,

    val username: String,

    @ColumnInfo(name = "is_sent")
    val isSent: Boolean = false,

    @ColumnInfo(name = "is_delivered")
    val isDelivered: Boolean = false,

    val sentDate: Instant = Clock.System.now(),

    @ColumnInfo(name = "is_new")
    val isNew: Boolean = false,
)
//sealed class Message(
//    open val id: String = "",
//    open val chatId: String,
//    open val body: String,
//    open val username: String,
//)

//data class SenderMessage(
//    override val id: String,
//    override val chatId: String,
//    override val body: String,
//    override val username: String,
//    val isSent: Boolean,
//    val isDelivered: Boolean,
//) : Message(id, chatId, body, username)
//
//data class ReceiverMessage(
//    override val id: String,
//    override val chatId: String,
//    override val body: String,
//    override val username: String,
//    val isNew: Boolean,
//) : Message(id, chatId, body, username)
//
//fun main() {
//    val messages = listOf<Message>()
//    messages.forEach {
//
//        when (it) {
//            is ReceiverMessage -> {
//                it.isNew
//            }
//            is SenderMessage -> {
//                it.isSent
//            }
//        }
//    }
//}