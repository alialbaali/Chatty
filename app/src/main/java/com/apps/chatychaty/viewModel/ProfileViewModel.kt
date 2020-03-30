package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.repo.UserRepository
import com.apps.chatychaty.token
import com.apps.chatychaty.ui.UpdateName
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

internal class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    internal lateinit var updateName: UpdateName

    internal lateinit var error: Error

    var imgPath = ""

    fun updatePhoto() {
        viewModelScope.launch {
            val file = File(imgPath)
            val body = file.asRequestBody("image/*".toMediaTypeOrNull())
            val mp = MultipartBody.Part.createFormData("img", file.name, body)
            userRepository.updatePhoto(token!!, mp)
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            try {
                userRepository.updateName(token!!, name)
                updateName.updateName(name)
            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }
        }
    }

}

internal class ProfileViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        }
        throw KotlinNullPointerException("Unknown ViewModel Class")
    }

}