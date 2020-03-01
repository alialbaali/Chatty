package com.apps.chatychaty.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.chatychaty.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel() : ViewModel() {

    lateinit var messages: LiveData<List<Message>>

    val currentMessage = MutableLiveData<Message>()


    init {
        viewModelScope.launch(Dispatchers.Main) {
            // TODO (init messages)
        }
        // Todo (init currentMessage)
    }
}

