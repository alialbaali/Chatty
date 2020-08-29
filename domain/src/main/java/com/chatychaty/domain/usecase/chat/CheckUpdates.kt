package com.chatychaty.domain.usecase.chat

import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository

class CheckUpdates(private val chatRepository: ChatRepository, private val messageRepository: MessageRepository) {

    suspend operator fun invoke() {
        val result = chatRepository.checkUpdates()
        result.onSuccess { value ->
            if (value.first || value.third) chatRepository.getRemoteChats()
            if (value.second) messageRepository.getRemoteMessages()
        }
    }
}