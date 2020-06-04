package com.chatychaty.domain.interactor.chat

import com.chatychaty.domain.interactor.message.GetMessages

class ChatUseCases(
    val createChat: CreateChat,
    val getChats: GetChats,
    val getMessages: GetMessages,
    val checkUpdates: CheckUpdates,
    val getRemoteChats: GetRemoteChats,
    val getChatById: GetChatById
)