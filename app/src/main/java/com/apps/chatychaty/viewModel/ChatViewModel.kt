package com.apps.chatychaty.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.chatychaty.model.Message
import com.apps.chatychaty.model.User

class ChatViewModel() : ViewModel() {

    val messages = MutableLiveData<MutableList<Message>>()

    val currentMessage = MutableLiveData<Message>()


    init {
        currentMessage.value = Message(0, "", user = User(0, "Ali Albaali", ""))
        messages.value = mutableListOf()
    }

    fun insertNote() {
        currentMessage.value?.text?.trim()
        messages.value?.add(currentMessage.value!!)
        currentMessage.value = Message(0, "", user = User(0, "Ali Albaali", ""))
    }
}

