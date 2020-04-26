package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.chatychaty.repo.UserRepository

class SearchViewModel(private val userRepository: UserRepository) : ViewModel() {

    val username = MutableLiveData<String>().also {
        it.value = ""
    }
}