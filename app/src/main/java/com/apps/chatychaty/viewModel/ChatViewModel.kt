package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.network.user
import com.apps.chatychaty.repo.MessageRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class ChatViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    val messages = MutableLiveData<List<Message>>()

    val currentMessage = MutableLiveData<Message>()


    init {
        currentMessage.value = Message(text = "", user = "alialbaali")
        getMessages()
    }

    private fun getMessages() {
        viewModelScope.launch {

            try {
                messages.postValue(messageRepository.getMessages())
            } catch (e: HttpException) {
                Timber.i(e.message())
            }

        }
    }

    fun postMessage() {
        viewModelScope.launch {
            if (!currentMessage.value?.text.isNullOrBlank()) {

                try {
                    messageRepository.postMessage(
                        currentMessage.value!!.also { it.text.trim() })
                    getMessages()
                    currentMessage.postValue(Message(user = user))
                } catch (e: HttpException) {
                    Timber.i(e.response().toString())
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