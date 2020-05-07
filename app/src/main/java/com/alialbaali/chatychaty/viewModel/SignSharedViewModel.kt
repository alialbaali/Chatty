package com.alialbaali.chatychaty.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alialbaali.model.User
import com.alialbaali.repository.UserRepository
import com.alialbaali.chatychaty.util.ExceptionHandler
import kotlinx.coroutines.*
import timber.log.Timber

class SignSharedViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main + ExceptionHandler.handler)

    val currentUser = MutableLiveData<User>()

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean> = _navigate

    private val _errors = MutableLiveData<List<String>>()
    val errors: LiveData<List<String>> = _errors

    init {
        viewModelScope.launch {
            currentUser.postValue(User())
        }
        _navigate.value = false
    }

    fun signUp() {
        coroutineScope.launch {
            val errors = userRepository.signUp(currentUser.value!!)
            if (errors != null) {
                _errors.postValue(errors)
            } else {
                onNavigate(true)
            }
        }
    }

    fun signIn() {
        coroutineScope.launch {
            val errors = userRepository.signIn(currentUser.value!!)
            if (errors != null) {
                _errors.postValue(errors)
            } else {
                _navigate.value = true
            }
        }
    }

    fun onNavigate(value : Boolean){
        _navigate.value = value
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}