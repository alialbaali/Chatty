package com.chatychaty.domain.interactor.chat

import com.chatychaty.domain.repository.ChatRepository

class GetChatById(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(chatId: Int) = chatRepository.getChatById(chatId)

}