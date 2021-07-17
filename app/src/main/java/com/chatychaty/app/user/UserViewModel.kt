package com.chatychaty.app.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.asUiState
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

class UserViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val mutableUser = MutableStateFlow<UiState<User>>(UiState.Loading)
    val user get() = mutableUser.asStateFlow()

    private val mutableTheme = MutableStateFlow<UiState<Theme>>(UiState.Loading)
    val theme get() = mutableTheme.asStateFlow()

    private val mutableState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val state get() = mutableState.asStateFlow()

    val name = MutableStateFlow("")
    val currentPassword = MutableStateFlow("")
    val newPassword = MutableStateFlow("")
    val newPasswordConfirm = MutableStateFlow("")
    val feedbackText = MutableStateFlow("")

    init {
        userRepository.getCurrentUser()
            .onEach {
                it?.let {
                    mutableUser.value = UiState.Success(it)
                    name.value = it.name
                }
            }
            .launchIn(viewModelScope)

        userRepository.getTheme()
            .onEach { mutableTheme.value = UiState.Success(it) }
            .launchIn(viewModelScope)
    }


    fun updateImage(byteArray: ByteArray, fileName: String) {
        viewModelScope.launch {
            mutableState.value = userRepository.updateImage(byteArray, fileName)
                .asUiState()
        }
    }

    fun updateName() {
        viewModelScope.launch {
            mutableState.value = userRepository.updateName(name.value)
                .asUiState()
        }
    }

    fun updatePassword() {
        viewModelScope.launch {
            mutableState.value = userRepository.updatePassword(currentPassword.value, newPassword.value)
                .asUiState()
        }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            userRepository.updateTheme(theme)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            chatRepository.deleteChats()
            messageRepository.deleteMessages()
            userRepository.signOut()
        }
    }

}