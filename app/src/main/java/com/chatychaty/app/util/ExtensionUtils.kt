package com.chatychaty.app.util

import android.annotation.SuppressLint
import android.view.View
import com.chatychaty.app.R
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message
import com.google.android.material.snackbar.Snackbar
import kotlinx.datetime.*
import java.text.SimpleDateFormat

private val EMPTY_MESSAGE = Message(chatId = "", username = "", body = "")

fun View.snackbar(
    value: String,
    length: Int = Snackbar.LENGTH_SHORT,
    anchorView: View? = null,
    actionText: String? = null,
    actionCallback: View.OnClickListener? = null,
) = Snackbar.make(this, value, length)
    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
    .setAnchorView(anchorView)
    .setAction(actionText, actionCallback)
    .show()

fun Message.getDisplayTime(is24HourFormat: Boolean): String {
    val timeZone = TimeZone.currentSystemDefault()
    val dateTime = sentDate.toLocalDateTime(timeZone)
    val currentDateTime = Clock.System.now().toLocalDateTime(timeZone)
    val time = "${dateTime.hour}:${dateTime.minute}".toLocalTime(is24HourFormat)

    return when (val dayDiff = currentDateTime.dayOfYear - dateTime.dayOfYear) {
        0 -> when (val hourDiff = currentDateTime.hour - dateTime.hour) {
            0 -> when (val minuteDiff = currentDateTime.minute - dateTime.minute) {
                0 -> "Now"
                in 0..60 -> minuteDiff.toString().plus("m")
                else -> error("Wrong Minute")
            }
            in 1..24 -> time
            else -> error("Wrong Hour")
        }
        in 1..7 -> "${dateTime.dayOfWeek.shortName} $time"
        else -> "${dateTime.dayOfWeek.shortName}, ${dateTime.month.shortName} ${dateTime.dayOfMonth} $time"
    }
}

@SuppressLint("SimpleDateFormat")
private fun String.toLocalTime(is24HourFormat: Boolean): String {
    val dateFormatIn24 = SimpleDateFormat("HH:mm")
    val dateFormatIn12 = SimpleDateFormat("h:mm aa")
    val dateIn24 = dateFormatIn24.parse(this)
    return if (is24HourFormat)
        this
    else
        dateFormatIn12.format(dateIn24)
}

private val DayOfWeek.shortName
    get() = name
        .lowercase()
        .replaceFirstChar { it.uppercase() }
        .take(3)

private val Month.shortName
    get() = name
        .lowercase()
        .replaceFirstChar { it.uppercase() }
        .take(3)

val Message.statusDrawable: Int
    get() {
        return when {
            isDelivered -> R.drawable.ic_round_done_all_24
            isSent -> R.drawable.ic_round_done_24
            else -> R.drawable.ic_round_schedule_24
        }
    }

fun List<Chat>.pairWithMessages(messages: List<Message>): List<Pair<Chat, Message>> {
    val messagesById = messages.associateBy { it.chatId }
    return map { chat ->
        val message = messagesById[chat.chatId]
        if (message != null)
            chat to message
        else
            chat to EMPTY_MESSAGE
    }
}

fun List<Pair<Chat, Message>>.sortedByIsPinnedAndSentDate() =
    sortedWith(
        compareByDescending<Pair<Chat, Message>> { it.first.isPinned }
            .thenByDescending { it.second.sentDate }
    )