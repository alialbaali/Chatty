package com.chatychaty.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    val username = MutableStateFlow("")

    private val mutableState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val state get() = mutableState.asStateFlow()

    fun createChat() {
        viewModelScope.launch {

            mutableState.value = UiState.Loading

            chatRepository.createChat(username.value)
                .fold(
                    onSuccess = {
                        mutableState.value = UiState.Success("User is found!")
                    },
                    onFailure = {
                        mutableState.value = UiState.Failure(it)
                    }
                )
        }
    }

}