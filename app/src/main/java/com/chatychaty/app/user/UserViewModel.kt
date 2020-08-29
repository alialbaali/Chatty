package com.chatychaty.app.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatychaty.domain.interactor.user.UserUseCase
import com.chatychaty.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _theme = MutableLiveData<String>()
    val theme: LiveData<String> = _theme

    val currentPassword = MutableLiveData<String>()

    val newPassword = MutableLiveData<String>()

    val newPasswordConfirm = MutableLiveData<String>()

    init {
        getThemeValue()
    }

    fun updatePhoto(byteArray: ByteArray, fileName: String) {
        GlobalScope.launch {
            userUseCase.updatePhoto(byteArray, fileName)
        }
    }

    fun getUser() {
        viewModelScope.launch {
            userUseCase.getUser().onSuccess { flow ->
                flow.collect { user ->
                    _user.postValue(user)
                }
            }
        }
    }

    fun getChatUserById(chatId: Int) {
        viewModelScope.launch {
            userUseCase.getChatUser(chatId).onSuccess { flow ->
                flow.collect { user ->
                    _user.postValue(user)
                }
            }
        }
    }

    private fun getThemeValue() {
        viewModelScope.launch {
            _theme.postValue(userUseCase.getTheme())
        }
    }

    fun setThemeValue(value: String) {
        viewModelScope.launch {
            userUseCase.setTheme(value)
            _theme.postValue(userUseCase.getTheme())
        }
    }

    fun updateName(name: String) {
        GlobalScope.launch {
            userUseCase.updateName(name)
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.signOut()
        }
    }

    fun changePassword() {
        viewModelScope.launch {

        }
    }
}