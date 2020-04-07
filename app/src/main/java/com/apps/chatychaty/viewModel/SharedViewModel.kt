package com.apps.chatychaty.viewModel

import androidx.lifecycle.*
import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.repo.ChatRepository
import com.apps.chatychaty.repo.MessageRepository
import com.apps.chatychaty.token
import com.apps.chatychaty.util.ExceptionHandler
import kotlinx.coroutines.*

internal class SharedViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val error: Error
) :
    ViewModel() {

    internal lateinit var chats: LiveData<List<Chat>>

    internal lateinit var messages: LiveData<List<Message>>

    val message = MutableLiveData<Message>()

    private val coroutineScope =
        CoroutineScope(Job() + Dispatchers.Main + ExceptionHandler.handler)

    internal fun getChats() {
        viewModelScope.launch {
            chats = chatRepository.getChatsDao()
        }
    }

    internal fun getLiveDataMessages(chatId: Int) {
        viewModelScope.launch {

            messages = messageRepository.getMessagesDao(chatId)

            message.postValue(Message(chatId = chatId))
        }
    }

    internal fun insertChat(username: String) {
        coroutineScope.launch {

            chatRepository.insertChatClient(token!!, username).let { response ->

                if (response.condition) {

                    chatRepository.insertChatDao(Chat(response.chatId!!, response.user!!))

                } else {
                    error.snackbar(response.error.toString())
                }
            }
        }
    }

    internal fun insertMessage(chatId: Int) {
        coroutineScope.launch {

            messageRepository.insertMessageClient(token!!, message.value!!).let { message ->

                messageRepository.insertMessageDao(message)

            }
            message.postValue(Message(chatId = chatId))
        }
    }

    internal fun checkUpdates() {
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

    internal fun updateChats() {
        coroutineScope.launch {

            chatRepository.getChatsClient(token!!).let { chats ->

                chatRepository.updateChatsDao(chats)

            }
        }
    }

    internal fun updateMessages() {
        coroutineScope.launch {

            messageRepository.getMessagesClient(token!!, messageRepository.countDao())
                .let { messages ->
                    messageRepository.updateMessagesDao(messages)
                }
        }
    }

    internal fun isMessageDelivered(username: String) {
        coroutineScope.launch {

            val lastMessage = messages.value?.lastOrNull {
                it.username == username && it.delivered == false
            }

            if (lastMessage != null) {

                if (messageRepository.isMessageDelivered(lastMessage.messageId)) {
                    messageRepository.updateDelivered()
                }
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
}

internal class SharedViewModelFactory(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val error: Error
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(chatRepository, messageRepository, error) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}