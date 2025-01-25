package com.mindeck.presentation.state

open class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()

    inline fun <T, R> UiState<T>.mapSuccess(transform: (T) -> R): R? {
        return if (this is UiState.Success) {
            transform(this.data)
        } else {
            null
        }
    }
}