package com.chatychaty.data.util

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.TOKEN
import kotlinx.coroutines.CoroutineExceptionHandler

fun ConnectivityManager.isNetworkAvailable(): Boolean {
    val nw = activeNetwork
    val actNw = getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}