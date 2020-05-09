package com.alialbaali.chatychaty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alialbaali.chatychaty.token
import com.alialbaali.chatychaty.util.ExceptionHandler
import com.alialbaali.repository.UserRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main + ExceptionHandler.handler)

    fun updatePhoto(byteArray: ByteArray, fileName: String) {
        viewModelScope.launch {
            try {
                userRepository.updatePhoto(byteArray, fileName)
            } catch (e: Exception) {
                Timber.i(e)
            }
        }
    }

    fun updateName(name: String) {
        coroutineScope.launch {
            userRepository.updateName(token!!, name)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun clearSharedPreferences() {
        userRepository.clearSharedPreferences()
    }
}