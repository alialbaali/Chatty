package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.User
import com.apps.chatychaty.network.token
import com.apps.chatychaty.network.user
import com.apps.chatychaty.repo.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class SignSharedViewModel(private val userRepository: UserRepository) : ViewModel() {

    val currentUser = MutableLiveData<User>()

    val authorized = MutableLiveData<Boolean>()

    lateinit var logIn: LogIn

    init {
        viewModelScope.launch {
            currentUser.postValue(User())
            authorized.postValue(false)
        }
    }

    fun logIn() {
        viewModelScope.launch {
            try {
                val muser = currentUser.value!!
                userRepository.logIn(muser).also {
                    token = it.token
                    Timber.i("TOKEN $token")
                    user = muser.username
                    authorized.postValue(true)
                }
            } catch (e: HttpException) {
                logIn.showSnackbar(e.message())
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

interface LogIn {
    fun showSnackbar(value: String)
}