package com.chatychaty.domain.interactor.message

import com.chatychaty.domain.repository.MessageRepository

class GetRemoteMessages (private val messageRepository: MessageRepository){

    suspend operator fun invoke() = messageRepository.getRemoteMessages()

}