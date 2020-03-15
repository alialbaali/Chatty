package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.User
import com.apps.chatychaty.repo.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class SignSharedViewModel(private val userRepository: UserRepository) : ViewModel() {

    val currentUser = MutableLiveData<User>()

    internal lateinit var error: Error
    internal lateinit var logIn: LogIn
    internal lateinit var img: String

    init {
        viewModelScope.launch {
            currentUser.postValue(User())
        }
    }

    internal fun logIn() {
        viewModelScope.launch {
            try {

                currentUser.value!!.let { user ->

                    userRepository.logIn(user).also { response ->

                        logIn.putPreferences(user.username, response.token)
                    }

                }

            } catch (e: HttpException) {
                error.snackbar(e.message())
            }
        }
    }


    internal fun createAccount() {
        viewModelScope.launch {
            try {
                currentUser.value!!.let { user ->

                    val file = File(img)
                    val body = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val mp = MultipartBody.Part.createFormData("img", file.name, body)


                    userRepository.createAccount(user, mp).also { response ->

                        logIn.putPreferences(user.username, response.token)
                    }

                }

            } catch (e: HttpException) {
                error.snackbar(e.message())
            }
        }
    }

    fun deleteAllMessages() {
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

internal interface LogIn {
    fun putPreferences(username: String, token: String?)
}