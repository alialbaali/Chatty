package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository
import com.chatychaty.domain.repository.UserRepository


class SignOut(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) {

    suspend operator fun invoke() {
        userRepository.signOut()
        chatRepository.deleteChats()
        messageRepository.deleteMessages()
    }

}
