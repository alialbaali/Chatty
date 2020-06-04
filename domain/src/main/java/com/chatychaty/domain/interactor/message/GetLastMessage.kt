package com.chatychaty.domain.interactor.message

import com.chatychaty.domain.repository.MessageRepository

class GetLastMessage(private val messageRepository: MessageRepository) {

    suspend operator fun invoke(chatId: Int) = messageRepository.getLastMessage(chatId)

}
