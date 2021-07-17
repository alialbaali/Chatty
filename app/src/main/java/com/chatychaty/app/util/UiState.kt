package com.chatychaty.app.util

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val value: T) : UiState<T>
    data class Failure(val exception: Throwable) : UiState<Nothing>
}

fun <T> Result<T>.asUiState(): UiState<T> = fold(
    onSuccess = { UiState.Success(it) },
    onFailure = { UiState.Failure(it) }
)