package com.chatychaty.domain.interactor.message

class MessageUseCase(
    val createMessage: CreateMessage,
    val getLastMessage: GetLastMessage,
    val getRemoteMessages: GetRemoteMessages
)