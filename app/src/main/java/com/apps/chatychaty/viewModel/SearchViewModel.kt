package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.User
import com.apps.chatychaty.repo.UserRepository
import kotlinx.coroutines.launch

internal class SearchViewModel(private val userRepository: UserRepository) : ViewModel() {

    val user = MutableLiveData<User>()

    val users = MutableLiveData<List<User>>()

    init {
        user.value = User()

        viewModelScope.launch {
            users.postValue(listOf())
        }
    }

    internal fun getUsers() {
        viewModelScope.launch {
            users.postValue(userRepository.getUsers(user.value!!))
        }
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