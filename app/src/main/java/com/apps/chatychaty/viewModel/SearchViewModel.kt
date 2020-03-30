package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.chatychaty.repo.UserRepository

internal class SearchViewModel(private val userRepository: UserRepository) : ViewModel() {

    val username = MutableLiveData<String>()

//    val users = MutableLiveData<List<User>>()

    init {
        username.value = ""
//        viewModelScope.launch {
//            users.postValue(listOf())
//        }
    }
}

internal class SearchViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(userRepository) as T
        }
        throw KotlinNullPointerException("Unknown ViewModel Class")
    }


}