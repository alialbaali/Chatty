package com.chatychaty.app.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MessageListViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val chatId: String,
) : ViewModel() {

    private val mutableChat = MutableStateFlow<UiState<Chat>>(UiState.Loading)
    val chat get() = mutableChat.asStateFlow()

    private val mutableMessages = MutableStateFlow<UiState<List<Message>>>(UiState.Loading)
    val messages get() = mutableMessages.asStateFlow()

    val searchTerm = MutableStateFlow("")

    val messageBody = MutableStateFlow("")

    init {
        chatRepository.getChatById(chatId)
            .onEach { mutableChat.value = UiState.Success(it) }
            .launchIn(viewModelScope)

        messageRepository.getMessages(chatId)
            .map { it.sortedBy { message -> message.sentDate } }
            .onEach { mutableMessages.value = UiState.Success(it) }
            .onEach { messageRepository.updateNewMessages() }
            .launchIn(viewModelScope)
    }

    fun createMessage() {
        viewModelScope.launch {
            val message = Message(chatId = chatId, body = messageBody.value.trim(), username = "")
            messageRepository.createMessage(message)
        }
    }

    fun archiveChat() {
        viewModelScope.launch {
            chatRepository.archiveChat((mutableChat.value as UiState.Success).value.chatId)
        }
    }

    fun unarchiveChat() {
        viewModelScope.launch {
            chatRepository.unarchiveChat((mutableChat.value as UiState.Success).value.chatId)
        }
    }

    fun pinChat() {
        viewModelScope.launch {
            chatRepository.pinChat((mutableChat.value as UiState.Success).value.chatId)
        }
    }

    fun unpinChat() {
        viewModelScope.launch {
            chatRepository.unpinChat((mutableChat.value as UiState.Success).value.chatId)
        }
    }

    fun muteChat() {
        viewModelScope.launch {
            chatRepository.muteChat((mutableChat.value as UiState.Success).value.chatId)
        }
    }

    fun unmuteChat() {
        viewModelScope.launch {
            chatRepository.unmuteChat((mutableChat.value as UiState.Success).value.chatId)
        }
    }

}