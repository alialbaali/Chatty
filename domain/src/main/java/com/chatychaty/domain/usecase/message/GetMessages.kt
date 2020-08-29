package com.chatychaty.domain.interactor.message

import com.chatychaty.domain.repository.MessageRepository


class GetMessages(private val messageRepository: MessageRepository) {

    suspend operator fun invoke(chatId: Int) = messageRepository.getMessages(chatId)

}