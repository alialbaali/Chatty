package com.chatychaty.app.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.pairWithMessages
import com.chatychaty.app.util.sortedByIsPinnedAndSentDate
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.flow.*

class ChatListViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
) : ViewModel() {

    private val mutableChatMessages = MutableStateFlow<UiState<List<Pair<Chat, Message>>>>(UiState.Loading)
    val chatMessages get() = mutableChatMessages.asStateFlow()

    private val mutableFilteredChatMessages = MutableStateFlow<List<Pair<Chat, Message>>>(emptyList())
    val filteredChatMessages get() = mutableFilteredChatMessages.asStateFlow()

    val searchTerm = MutableStateFlow("")

    init {
        combine(chatRepository.getChats(), messageRepository.getLastMessages()) { chats, messages ->
            chats
                .pairWithMessages(messages)
                .sortedByIsPinnedAndSentDate()
        }
            .onEach { mutableChatMessages.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }

    fun filterChats() {
        mutableFilteredChatMessages.value = (mutableChatMessages.value as UiState.Success).value
            .filter { pair ->
                val term = searchTerm.value
                    .filterNot { it.isWhitespace() }

                pair.first.name.contains(term, ignoreCase = true) || pair.first.username.contains(term, ignoreCase = true)
            }
    }

}