package com.mindeck.presentation.state

open class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()

    inline fun <T, R> UiState<T>.mapSuccess(transform: (T) -> R): UiState<R> {
        return when (this) {
            is Success -> Success(transform(this.data))
            is Error -> Error(this.exception)
            else -> Loading
        }
    }

    inline fun <T, R> UiState<List<T>>.mapToUiState(transform: (List<T>) -> List<R>): UiState<List<R>> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(exception)
            else -> Loading
        }
    }
}