package com.chatychaty.app.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.domain.interactor.user.UserUseCase
import com.chatychaty.domain.model.User
import kotlinx.coroutines.launch

class SignSharedViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    val currentUser = MutableLiveData<User>()

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean> = _navigate

    private val _errors = MutableLiveData<String>()
    val errors: LiveData<String> = _errors

    init {
        viewModelScope.launch {
            currentUser.postValue(User())
        }
        _navigate.value = false
    }

    fun signUp() {
        viewModelScope.launch {
            val result = userUseCase.signUp(currentUser.value!!).exceptionOrNull()

            if (result == null)
                onNavigate(true)
            else
                _errors.postValue(result.message)
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val result = userUseCase.signIn(currentUser.value!!).exceptionOrNull()

            if (result == null)
                onNavigate(true)
            else
                _errors.postValue(result.message)
        }
    }

    fun onNavigate(value: Boolean) {
        _navigate.value = value
    }

}