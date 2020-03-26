package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.User
import com.apps.chatychaty.repo.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

internal class SearchViewModel(private val userRepository: UserRepository) : ViewModel() {

    val user = MutableLiveData<User>()

    internal lateinit var error: Error

    internal lateinit var navigateToList: NavigateToList

    internal var token = ""

//    val users = MutableLiveData<List<User>>()

    init {
        user.value = User()

//        viewModelScope.launch {
//            users.postValue(listOf())
//        }
    }

    internal fun getUser() {
        viewModelScope.launch {
            try {

                userRepository.getUser(token, user.value!!.username).let { response ->
                    if (response.error.isNullOrBlank()) {

                        navigateToList.navigate(
                            response.user!!,
                            response.chatId!!
                        )

                    } else {
                        error.snackbar(response.error)
                    }
                }
            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }
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

internal interface NavigateToList {
    fun navigate(user: User, chatId: Int)
}