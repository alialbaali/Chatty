package com.chatychaty.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProfileViewModel(private val chatRepository: ChatRepository, private val chatId: String) : ViewModel() {

    private val mutableChat = MutableStateFlow<UiState<Chat>>(UiState.Loading)
    val chat get() = mutableChat.asStateFlow()

    init {
        chatRepository.getChatById(chatId)
            .onEach { mutableChat.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }


}