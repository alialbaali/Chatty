package com.chatychaty.domain.interactor.chat

import com.chatychaty.domain.repository.ChatRepository


class CreateChat(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(username: String) = chatRepository.createChat(username)

}