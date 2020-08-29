package com.chatychaty.domain.interactor.chat

import com.chatychaty.domain.repository.ChatRepository


class GetChats(private val chatRepository: ChatRepository) {

     suspend operator fun invoke() = chatRepository.getChats()

}