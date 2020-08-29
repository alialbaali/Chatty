package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.ChatRepository
import kotlinx.coroutines.flow.map

class GetChatUser(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(chatId: Int) = chatRepository.getChatById(chatId).mapCatching {

        return@mapCatching it.map { chat ->

            return@map chat.user

        }
    }
}
