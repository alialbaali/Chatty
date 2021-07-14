package com.chatychaty.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

private const val NETWORK_ERROR = "Please connect to the internet!"
private const val UNKNOWN_ERROR = "Something went wrong! please try again later"
private const val BEARER_SCHEME = "Bearer "

internal suspend fun Flow<String>.asBearerToken(): String = map { BEARER_SCHEME + it }.first()

@Suppress("UNCHECKED_CAST")
internal suspend inline fun <R> request(dispatcher: CoroutineDispatcher = Dispatchers.IO, crossinline block: suspend () -> (R)): Result<R> =
    withContext(dispatcher) {
        try {
            Result.success(block())
        } catch (throwable: Throwable) {
            throwable.parseError()
        }
    }

private fun <R> Throwable.parseError(): Result<R> = when (this) {

    is UnknownHostException -> Result.failure(Throwable(NETWORK_ERROR))

    is ConnectException -> Result.failure(Throwable(NETWORK_ERROR))

    is HttpException -> {
        response()
            ?.errorBody()
            ?.string()
            ?.let { errorBody ->

                val errors = JSONObject(errorBody)
                    .run { get("errors") as JSONArray }
                    .join(", ")
                    .replace("\"", "")
                    .replace(".", "")
                    .plus(".")

                Result.failure(Throwable(errors))

            } ?: Result.failure(Throwable(UNKNOWN_ERROR))
    }

    else -> Result.failure(this)
}