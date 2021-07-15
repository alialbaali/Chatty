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

    private val mutableSelectedMessage = MutableStateFlow<UiState<Message>>(UiState.Empty)
    val selectedMessage get() = mutableSelectedMessage.asStateFlow()

    fun selectMessage(messageId: String) {
        mutableSelectedMessage.value = UiState.Loading
        messageRepository.getMessageById(messageId)
            .onEach { mutableSelectedMessage.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }

}