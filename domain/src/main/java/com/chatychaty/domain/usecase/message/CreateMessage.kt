package com.chatychaty.domain.interactor.message

import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.MessageRepository


class CreateMessage(private val messageRepository: MessageRepository) {

    suspend operator fun invoke(message: Message) = messageRepository.createMessage(message)

}