package com.chatychaty.app.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    val username = MutableLiveData<String>().also {
        it.value = ""
    }
}