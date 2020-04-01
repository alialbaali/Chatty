package com.apps.chatychaty.viewModel

import androidx.lifecycle.*
import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.repo.ChatRepository
import com.apps.chatychaty.repo.MessageRepository
import com.apps.chatychaty.token
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

internal class SharedViewModel(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val error: Error
) :
    ViewModel() {

    internal lateinit var chats: LiveData<List<Chat>>

    internal lateinit var messages: LiveData<List<Message>>

    val message = MutableLiveData<Message>()

    init {
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
        viewModelScope.launch {

            try {

                chatRepository.insertChatClient(token!!, username).let { response ->

                    if (response.condition) {

                        chatRepository.insertChatDao(Chat(response.chatId!!, response.user!!))

                    } else {
                        error.snackbar(response.error.toString())
                    }
                }

            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }
        }
    }

    internal fun insertMessage(chatId: Int) {
        viewModelScope.launch {

            try {

                messageRepository.insertMessageClient(token!!, message.value!!).let { message ->

                    messageRepository.insertMessageDao(message.apply {
                        this.delivered = false
                    })

                }

                message.postValue(Message(chatId = chatId))

            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }
        }
    }

    internal fun checkUpdates() {
        viewModelScope.launch {

            try {

                chatRepository.checkUpdates().let {

                    if (it.chatUpdate) {
                        updateChats()
                    }

                    if (it.messageUpdate) {
                        updateMessages()
                    }

                }

            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }

        }
    }

    internal fun updateChats() {
        viewModelScope.launch {

            try {

                chatRepository.getChatsClient(token!!).let { chats ->

                    chatRepository.updateChatsDao(chats)

                }

            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }

        }
    }

    internal fun updateMessages() {
        viewModelScope.launch {
            try {

                messageRepository.getMessagesClient(token!!, messageRepository.countDao())
                    .let { messages ->
                        messageRepository.updateMessagesDao(messages)
                    }

            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }
        }
    }

    internal fun isMessageDelivered(username: String) {
        viewModelScope.launch {

            val lastMessage = messages.value?.lastOrNull {
                it.username == username && it.delivered == false
            }

            try {

                if (lastMessage != null) {

                    if (messageRepository.isMessageDelivered(lastMessage.messageId)) {
                        messageRepository.updateDelivered()
                    }
                }

            } catch (e: HttpException) {
                error.snackbar(e.response().toString())
            }
        }
    }

    fun getLastMessage(chatId: Int): String {
        return runBlocking {
            messageRepository.getLastMessage(chatId)
        }
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