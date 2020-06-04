package com.chatychaty.domain.interactor.chat

import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository

class CheckUpdates(private val chatRepository: ChatRepository, private val messageRepository: MessageRepository) {

    suspend operator fun invoke() {
        chatRepository.checkUpdates().also {
            if (it.first) {
                chatRepository.getRemoteChats()
            }
            if (it.second) {
                messageRepository.getRemoteMessages()
            }
        }
    }
}