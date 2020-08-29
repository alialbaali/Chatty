package com.chatychaty.data

import com.chatychaty.data.source.remote.schema.Response
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

internal const val NETWORK_ERROR = "Please connect to the internet!"
internal const val UNKNOWN_ERROR = "Something went wrong! please try again later"

@Suppress("UNCHECKED_CAST")
internal inline fun <R> tryCatching(block: () -> (R)): Result<R> {

    return try {
        Result.success(block())
    } catch (throwable: Throwable) {

        when (throwable) {

            is UnknownHostException -> Result.failure(Throwable(NETWORK_ERROR))

            is ConnectException -> Result.failure(Throwable(NETWORK_ERROR))

            is HttpException -> {
                val errorBody = throwable.response()?.errorBody()?.string() ?: return Result.failure(Throwable(UNKNOWN_ERROR))
                val errors = JSONObject(errorBody).get("errors") as List<String>?
                Result.failure(Throwable(errors.toString()))
            }

            else -> Result.failure(throwable)

        }
    }
}

fun <T> Response<T>.retrieveErrors(): String = this.errors?.find { error -> error.isNotBlank() }.toString()