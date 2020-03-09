package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.repo.MessageRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChatViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    internal val messages = MutableLiveData<List<Message>>()

    val currentMessage = MutableLiveData<Message>()

    internal var token = MutableLiveData<String>()

    internal var username = ""

    internal val success = MutableLiveData<Boolean>()

    internal lateinit var error: Error

    init {
        currentMessage.value = Message()
        getMessages()
        success.value = false
    }

    internal fun getMessages() {
        viewModelScope.launch {

            try {
                messages.postValue(messageRepository.getMessages())
            } catch (e: HttpException) {
                error.snackbar(e.message())
            }

        }
    }

    internal fun postMessage() {
        viewModelScope.launch {

            if (!currentMessage.value?.text.isNullOrBlank()) {

                try {
                    messageRepository.postMessage(
                        currentMessage.value!!.also { it.text.trim() },
                        token.value!!
                    )

                    currentMessage.value = Message()

                } catch (e: HttpException) {
                    error.snackbar(e.message())
                }

            }
        }
    }


    internal fun getNewMessages(id: Int): Boolean {
        viewModelScope.launch {
            try {


                if (messageRepository.getNewMessages(id).isNotEmpty()) {
                    success.postValue(true)
                }
            } catch (e: HttpException) {
                error.snackbar(e.message())
            }
        }
        return success.value ?: false
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