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

    private var hubConnection: HubConnection? = null

    override fun connect(token: String) {
        if (hubConnection == null)
            hubConnection = HubConnectionBuilder.create(HUB_URL)
                .withAccessTokenProvider(Single.just(token))
                .build()

        hubConnection?.start()
    }

    override fun collectMessages(callback: (List<RemoteMessage>) -> Unit) {
        hubConnection?.on(
            UPDATE_MESSAGES,
            callback,
            MessageListType.type,
        )
    }

    override fun collectMessageStatus(callback: (RemoteMessage) -> Unit) {
        hubConnection?.on(
            UPDATE_MESSAGE_STATUS,
            callback,
            MessageStatusType.type,
        )
    }

    override fun collectChat(callback: (RemoteChat) -> Unit) {
        hubConnection?.on(
            UPDATE_CHAT,
            callback,
            ChatType.type,
        )
    }

    override fun disconnect() {
        if (hubConnection != null)
            try {
                hubConnection?.stop()
            } catch (exception: Throwable) {

            }
    }

    private object MessageListType : TypeReference<List<RemoteMessage>>()
    private object MessageStatusType : TypeReference<RemoteMessage>()
    private object ChatType : TypeReference<RemoteChat>()
}