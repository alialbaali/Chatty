package com.chatychaty.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.asUiState
import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignSharedViewModel(private val userRepository: UserRepository) : ViewModel() {

    val username = MutableStateFlow("")
    val name = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val mutableState = MutableStateFlow<UiState<User>>(UiState.Empty)
    val state get() = mutableState.asStateFlow()

    fun signUp() {
        viewModelScope.launch {
            mutableState.value = UiState.Loading

            val user = createUser()

            mutableState.value = userRepository.signUp(user)
                .asUiState()
        }
    }

    fun signIn() {
        viewModelScope.launch {
            mutableState.value = UiState.Loading

            val user = createUser()

            mutableState.value = userRepository.signIn(user)
                .asUiState()
        }
    }

    private fun createUser() = User(name.value, username.value, password.value, null)

}