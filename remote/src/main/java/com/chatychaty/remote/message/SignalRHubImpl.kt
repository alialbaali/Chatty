package com.chatychaty.remote.message

import com.chatychaty.remote.chat.RemoteChat
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.TypeReference
import io.reactivex.Single

private const val HUB_URL = "http://chatychaty0.herokuapp.com/v1/chatHub"
private const val UPDATE_MESSAGES = "UpdateMessages"
private const val UPDATE_CHAT = "UpdateChat"
private const val UPDATE_MESSAGE_STATUS = "UpdateMessageStatus"

class SignalRHubImpl : SignalRHub {

    private lateinit var hubConnection: HubConnection

    override fun connect(token: String) {

        if (!this::hubConnection.isInitialized)
            hubConnection = HubConnectionBuilder.create(HUB_URL)
                .withAccessTokenProvider(Single.just(token))
                .build()

        hubConnection.start()

    }

    override fun collectMessages(callback: (List<RemoteMessage>) -> Unit) {
        hubConnection.on(
            UPDATE_MESSAGES,
            callback,
            MessageListType.type,
        )
    }

    override fun collectMessageStatus(callback: (RemoteMessage) -> Unit) {
        hubConnection.on(
            UPDATE_MESSAGE_STATUS,
            callback,
            MessageStatusType.type,
        )
    }

    override fun collectChat(callback: (RemoteChat) -> Unit) {
        hubConnection.on(
            UPDATE_CHAT,
            callback,
            ChatType.type,
        )
    }

    override fun disconnect() {
        if (this::hubConnection.isInitialized)
            hubConnection.stop()
    }

    private object MessageListType : TypeReference<List<RemoteMessage>>()
    private object MessageStatusType : TypeReference<RemoteMessage>()
    private object ChatType : TypeReference<RemoteChat>()
}

fun main() {
    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6IndvcmsiLCJuYW1laWQiOiI3OTdkODA4ZS1lYWI1LTQwOWItYTIwYi03ZGY2Yzg1NDU1NjAiLCJqdGkiOiJjMjlkMDY3NS0yMmZjLTRlOTUtOWQwMy01NmZlMDJhYzM3OGQiLCJuYmYiOjE2MjQ0NzM5MDAsImV4cCI6MTYyNTA3ODcwMCwiaWF0IjoxNjI0NDczOTAwLCJpc3MiOiJodHRwczovL2NoYXR5Y2hhdHkwLmhlcm9rdWFwcC5jb20vIiwiYXVkIjoiaHR0cHM6Ly9jaGF0eWNoYXR5MC5oZXJva3VhcHAuY29tLyJ9.oJhESv4Ewx0_7RYBzJ3Kgbqj42Mn45lFsya9M5LqGzU"
    SignalRHubImpl().apply {
        connect(token)
        collectMessages {
            println(it)
        }
        collectMessageStatus {
            println(it)
        }
        collectChat {
            println(it)
        }
    }
}