package com.chatychaty.app.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MessageItemViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    private val mutableMessage = MutableStateFlow<UiState<Message>>(UiState.Loading)
    val message get() = mutableMessage.asStateFlow()

    fun selectMessage(messageId: String) {
        messageRepository.getMessageById(messageId)
            .onEach { mutableMessage.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }

}