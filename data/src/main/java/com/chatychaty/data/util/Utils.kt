package com.chatychaty.data.util

import java.net.UnknownHostException

inline fun <R> tryCatching(block: () -> (R)): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        when (e) {
            is UnknownHostException -> Result.failure(Throwable("Please connect to the internet!"))
            else -> Result.failure(Throwable("Something went wrong! Please try again later"))
        }
    }
}

