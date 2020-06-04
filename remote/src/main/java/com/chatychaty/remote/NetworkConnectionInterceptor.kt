package com.chatychaty.remote

import okhttp3.Interceptor
import okhttp3.Response

abstract class NetworkConnectionInterceptor : Interceptor {

    abstract fun isNetworkAvailable(): Boolean

    abstract fun onNetworkUnavailable()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!isNetworkAvailable()) {
            onNetworkUnavailable()
        }
        return chain.proceed(request)

    }
}

interface NetworkConnectionListener {
    fun onInternetUnavailable()
}