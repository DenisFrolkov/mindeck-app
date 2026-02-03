package com.mindeck.presentation.state

import androidx.compose.runtime.Composable

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()

    inline fun <T, R> UiState<T>.mapData(transform: (T) -> R): UiState<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(exception)
            is Loading -> Loading
        }
    }
}

inline fun <T> UiState<T>.onSuccess(action: (T) -> Unit): UiState<T> {
    if (this is UiState.Success) action(data)
    return this
}

inline fun <T> UiState<T>.onError(action: (Throwable) -> Unit): UiState<T> {
    if (this is UiState.Error) action(exception)
    return this
}

inline fun <T> UiState<T>.onLoading(action: () -> Unit): UiState<T> {
    if (this is UiState.Loading) action()
    return this
}

fun <T> UiState<T>.getOrNull(): T? {
    return if (this is UiState.Success) this.data else null
}

@Composable
inline fun <T> UiState<T>.RenderUiState(
    onSuccess: @Composable (T) -> Unit,
    onLoading: @Composable () -> Unit,
    onError: @Composable (Throwable) -> Unit,
) {
    when (this) {
        is UiState.Success -> onSuccess(data)
        is UiState.Loading -> onLoading()
        is UiState.Error -> onError(exception)
    }
}
