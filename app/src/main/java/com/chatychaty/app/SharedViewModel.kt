package com.chatychaty.app

import androidx.lifecycle.*
import com.chatychaty.app.util.asLiveData
import com.chatychaty.domain.interactor.chat.ChatUseCases
import com.chatychaty.domain.interactor.message.MessageUseCase
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SharedViewModel(private val chatUseCases: ChatUseCases, private val messageUseCase: MessageUseCase) : ViewModel() {


    val chats = liveData<List<Chat>> {
        chatUseCases.getChats().onSuccess {
            emitSource(it.asLiveData())
        }.onFailure {
            _errors.postValue(it.message)
        }
    }

    val searchTerm = MutableLiveData<String>()

    private val _isDragEnabled = MutableLiveData<Boolean>()
    val isDragEnabled: LiveData<Boolean> = _isDragEnabled

    private val _chat = MutableLiveData<Chat>()
    val chat = _chat.asLiveData()

    private val _messages = MutableLiveData<List<Message>>()
    val messages = _messages.asLiveData()

    private val _errors = MutableLiveData<String>()
    val errors: LiveData<String> = _errors

    private val _message = MutableLiveData<Message>()
    val message = _message.asLiveData()


    fun getMessages(chatId: Int) = viewModelScope.launch {

        _message.postValue(Message(chatId = chatId))

        chatUseCases.getMessages(chatId).collect { result ->
            result.onSuccess { value ->
                _messages.postValue(value)
            }
        }

    }

    fun getChatById(chatId: Int) {
        viewModelScope.launch {
            chatUseCases.getChatById(chatId).onSuccess { flow ->
                flow.collect { chat ->
                    _chat.postValue(chat)
                }
            }
        }
    }

    fun createMessage(chatId: Int) {

        viewModelScope.launch {

            message.value?.let { message ->
                messageUseCase.createMessage(message)
            }

            _message.postValue(Message(chatId = chatId))

        }

    }

    fun createChat(username: String) {
        viewModelScope.launch {
            chatUseCases.createChat(username).also {
                _errors.postValue(it.exceptionOrNull()?.message)
            }

        }
    }

    fun checkUpdates() {
        viewModelScope.launch {
            chatUseCases.checkUpdates()
        }
    }

    //    fun isMessageDelivered(username: String) {
//        coroutineScope.launch {
//
//            val lastMessage = messages.value?.lastOrNull {
//                it.username == username && it.delivered == false
//            }
//
//            if (lastMessage != null) {
//                messageRepository.isMessageDelivered(lastMessage.messageId)
//            }
//        }
//    }
//


    suspend fun getRemoteChats() {
        viewModelScope.launch {
            chatUseCases.getRemoteChats()
        }
    }

    suspend fun getRemoteMessages() {
        viewModelScope.launch {
            messageUseCase.getRemoteMessages()
        }
    }

    fun setDrag(enabled: Boolean) {
        _isDragEnabled.value = enabled
    }

    fun getLastMessage(chatId: Int): String {
        return ""
//        runBlocking {
//            val x = messageUseCase.getLastMessage(chatId).getOrNull().collect {
//
//            }
//        }
    }
}