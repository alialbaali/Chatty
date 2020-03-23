package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.User
import com.apps.chatychaty.repo.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignSharedViewModel(private val userRepository: UserRepository) : ViewModel() {

    val currentUser = MutableLiveData<User>()

    internal lateinit var error: Error
    internal lateinit var sign: Sign

    init {
        viewModelScope.launch {
            currentUser.postValue(User())
        }
    }

    internal fun signUp() {
        viewModelScope.launch {
            try {
//                    val file = File(img)
//                    val body = file.asRequestBody("image/*".toMediaTypeOrNull())
//                    val mp = MultipartBody.Part.createFormData("img", file.name, body)

                userRepository.signUp(currentUser.value!!).also { response ->
                    if (response.error == null) {
                        sign.putPreferences(
                            response.token!!,
                            response.user.name,
                            response.user.username,
                            response.user.imgUrl
                        )
                    } else {
                        error.snackbar(response.error)
                    }
                }

            } catch (e: HttpException) {
                error.snackbar(e.message())
            }
        }
    }

    internal fun signIn() {
        viewModelScope.launch {
            try {

                userRepository.signIn(currentUser.value!!).also { response ->

                    if (response.error == null) {
                        sign.putPreferences(
                            response.token!!,
                            response.user.name,
                            response.user.username,
                            response.user.imgUrl
                        )
                    } else {
                        error.snackbar(response.error)
                    }
                }

            } catch (e: HttpException) {
                error.snackbar(e.message())
            }
        }
    }

    internal fun deleteAllMessages() {
        viewModelScope.launch {
            userRepository.deleteAllMessages()
        }
    }
}

internal class SignSharedViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignSharedViewModel::class.java)) {
            return SignSharedViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}

internal interface Error {
    fun snackbar(value: String)
}

internal interface Sign {
    fun putPreferences(token: String, name: String, username: String, imgUrl: String)
}