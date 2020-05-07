package com.alialbaali.chatychaty.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alialbaali.model.Chat
import com.alialbaali.model.Message
import com.alialbaali.repository.ChatRepository
import com.alialbaali.repository.MessageRepository
import com.alialbaali.chatychaty.token
import com.alialbaali.chatychaty.util.ExceptionHandler
import kotlinx.coroutines.*

class SharedViewModel(private val chatRepository: ChatRepository, private val messageRepository: MessageRepository) : ViewModel() {

    lateinit var chats: LiveData<List<Chat>>

    lateinit var messages: LiveData<List<Message>>

    private val _errors = MutableLiveData<String>()
    val errors: LiveData<String> = _errors

    val message = MutableLiveData<Message>()

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main + ExceptionHandler.handler)

    init {
        getChats()
    }

    private fun getChats() {
        viewModelScope.launch {
            chats = chatRepository.getChatsDao()
        }
    }

    fun getLiveDataMessages(chatId: Int) {
        viewModelScope.launch {

            messages = messageRepository.getMessagesDao(chatId)

            message.postValue(Message(chatId = chatId))
        }
    }

    fun insertChat(username: String) {
        coroutineScope.launch {
            val errors = chatRepository.insertChatClient(token!!, username)
            if (errors != null) {
                _errors.postValue(errors)
            }
        }
    }


    fun insertMessage(chatId: Int) {
        coroutineScope.launch {

            messageRepository.insertMessageClient(token!!, message.value!!)

            message.postValue(Message(chatId = chatId))

        }
    }

    fun checkUpdates() {
        coroutineScope.launch {

            chatRepository.checkUpdates().let {

                if (it.chatUpdate) {
                    updateChats()
                }

                if (it.messageUpdate) {
                    updateMessages()
                }

            }
        }
    }


    fun isMessageDelivered(username: String) {
        coroutineScope.launch {

            val lastMessage = messages.value?.lastOrNull {
                it.username == username && it.delivered == false
            }

            if (lastMessage != null) {
                messageRepository.isMessageDelivered(lastMessage.messageId)
            }
        }
    }

    fun getLastMessage(chatId: Int): String {
        return runBlocking {
            messageRepository.getLastMessage(chatId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun updateMessages() {
        coroutineScope.launch {
            messageRepository.getMessagesClient()
        }
    }

    fun updateChats() {
        coroutineScope.launch {
            chatRepository.getChatsClient()
        }
    }
}