package com.chatychaty.app.chat.archive

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

class ChatListArchiveViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val mutableArchivedChats = MutableStateFlow<UiState<List<Pair<Chat, Message>>>>(UiState.Loading)
    val archivedChats get() = mutableArchivedChats.asStateFlow()

    init {
        combine(chatRepository.getArchivedChats(), messageRepository.getLastMessages()) { chats, messages ->
            chats.pairWithMessages(messages)
                .sortedByIsPinnedAndSentDate()
        }
            .onEach { mutableArchivedChats.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }
}