package com.alialbaali.chatychaty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alialbaali.repository.UserRepository
import com.alialbaali.chatychaty.token
import com.alialbaali.chatychaty.util.ExceptionHandler
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main + ExceptionHandler.handler)

    fun updatePhoto(file: ByteArray) {
        viewModelScope.launch {
            try {
//                val body = file.toRequestBody("image/*".toMediaTypeOrNull())
//                val body = file.asRequestBody("image/*".toMediaTypeOrNull())
//                val mp = MultipartBody.Part.createFormData("img", "Image", body)
//                userRepository.updatePhoto(token!!, mp)
            } catch (e: Exception) {
                Timber.i(e)
            }
        }
    }

    fun updatePhoto(file: File) {
        viewModelScope.launch {
            try {
//                val body = file.toRequestBody("image/*".toMediaTypeOrNull())
//                val body = file.asRequestBody("image/*".toMediaTypeOrNull())
//                val mp = MultipartBody.Part.createFormData("img", "Image", body)
//                userRepository.updatePhoto(token!!, mp)
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