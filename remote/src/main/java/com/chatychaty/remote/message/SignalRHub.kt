package com.chatychaty.remote.message

import com.chatychaty.remote.chat.RemoteChat

interface SignalRHub {

    fun connect(token: String)

    fun collectMessages(callback: (List<RemoteMessage>) -> Unit)

    fun collectMessageStatus(callback: (RemoteMessage) -> Unit)

    fun collectChat(callback: (RemoteChat) -> Unit)

    fun disconnect()

}