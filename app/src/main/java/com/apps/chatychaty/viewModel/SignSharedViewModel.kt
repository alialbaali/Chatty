package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.User
import com.apps.chatychaty.repo.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class SignSharedViewModel(private val userRepository: UserRepository) : ViewModel() {

    val currentUser = MutableLiveData<User>()

    internal lateinit var error: Error
    internal lateinit var logIn: LogIn

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

                        if (!response.errors.isNullOrBlank()) {
                            error.snackbar(response.errors.toString())
                        }

                        logIn.putPreferences(user.username, response.token)
                    }

                }

            } catch (e: HttpException) {
//                error.snackbar(e.message())
            }
        }
    }

    internal fun createAccount() {
        viewModelScope.launch {
            try {

                currentUser.value!!.let { user ->

                    userRepository.createAccount(user).also { response ->

                        if (!response.errors.isNullOrBlank()) {
                            error.snackbar(response.errors.toString())
                        }

                        logIn.putPreferences(user.username, response.token)
                    }

                }

            } catch (e: HttpException) {
//                error.snackbar(e.message())
            }
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