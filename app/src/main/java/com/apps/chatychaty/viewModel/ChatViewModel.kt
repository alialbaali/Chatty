package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.repo.MessageRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class ChatViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    internal val messages = MutableLiveData<List<Message>>()

    val currentMessage = MutableLiveData<Message>()

    internal var token = MutableLiveData<String>()

    internal var username = ""

    internal lateinit var error: Error

    init {
        currentMessage.value = Message(user = "")
        getMessages()
    }

    internal fun getMessages() {
        viewModelScope.launch {

            try {
                messages.postValue(messageRepository.getMessages())
            } catch (e: HttpException) {
                Timber.i(e.message())
            }

        }
    }

    internal fun postMessage() {
        viewModelScope.launch {
            Timber.i("${currentMessage.value?.text}")
            if (!currentMessage.value?.text.isNullOrBlank()) {
                Timber.i("Post Message")
                try {
                    messageRepository.postMessage(
                        currentMessage.value!!.also { it.text.trim() },
                        token.value!!
                    )

                    currentMessage.value = Message(user = username)

                } catch (e: HttpException) {
                    error.snackbar(e.message())
                }

            }
        }
    }
}

internal class ChatViewModelFactory(private val messageRepository: MessageRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(messageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}