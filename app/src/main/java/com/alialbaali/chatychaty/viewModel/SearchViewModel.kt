package com.alialbaali.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alialbaali.repository.UserRepository

class SearchViewModel(private val userRepository: UserRepository) : ViewModel() {

    val username = MutableLiveData<String>().also {
        it.value = ""
    }
}