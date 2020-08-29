package com.chatychaty.domain.interactor.chat

import com.chatychaty.domain.repository.ChatRepository

class GetRemoteChats (private val chatRepository: ChatRepository){

    suspend operator fun invoke() = chatRepository.getRemoteChats()

}