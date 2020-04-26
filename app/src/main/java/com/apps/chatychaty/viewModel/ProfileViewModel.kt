package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.repo.UserRepository
import com.apps.chatychaty.token
import com.apps.chatychaty.util.ExceptionHandler
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main + ExceptionHandler.handler)

    fun updatePhoto(file: File) {
        coroutineScope.launch {
    //            val body = imgBytes.toRequestBody("image/*".toMediaTypeOrNull())
            val body = file.asRequestBody("image/*".toMediaTypeOrNull())
            val mp = MultipartBody.Part.createFormData("img", "Image", body)
            userRepository.updatePhoto(token!!, mp)
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
}