package com.chatychaty.app.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.domain.model.Theme
import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository
import com.chatychaty.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val mutableCurrentUser = MutableStateFlow<UiState<User>>(UiState.Loading)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    private val mutableTheme = MutableStateFlow<UiState<Theme>>(UiState.Loading)
    val theme get() = mutableTheme.asStateFlow()

    init {
        userRepository.getCurrentUser()
            .onEach {
                mutableCurrentUser.value = when (it) {
                    null -> UiState.Failure(Throwable("User isn't signed in"))
                    else -> UiState.Success(it)
                }
            }
            .launchIn(viewModelScope)

        userRepository.getTheme()
            .onEach { mutableTheme.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }

    fun connectHub() {
        viewModelScope.launch {
            messageRepository.connectHub()
        }
    }

    fun syncData() {
        viewModelScope.launch {
            messageRepository.syncMessages()
            chatRepository.syncChats()
        }
    }

    fun disconnectHub() {
        viewModelScope.launch {
            messageRepository.disconnectHub()
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            messageRepository.refreshMessages()
            chatRepository.refreshChats()
        }
    }
}