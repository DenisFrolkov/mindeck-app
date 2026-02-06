package com.mindeck.presentation.state

import androidx.compose.runtime.Composable

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val error: String) : UiState<Nothing>()
}

@Composable
inline fun <T> UiState<T>.RenderState(
    onSuccess: @Composable (T) -> Unit,
    onLoading: @Composable () -> Unit,
    onError: @Composable (String) -> Unit,
) {
    when (this) {
        is UiState.Success -> onSuccess(data)
        is UiState.Loading -> onLoading()
        is UiState.Error -> onError(error)
    }
}


inline fun <T> UiState<T>.onSuccess(action: (T) -> Unit): UiState<T> {
    if (this is UiState.Success) action(data)
    return this
}

inline fun <T> UiState<T>.onError(action: (String) -> Unit): UiState<T> {
    if (this is UiState.Error) action(error)
    return this
}

inline fun <T> UiState<T>.onLoading(action: () -> Unit): UiState<T> {
    if (this is UiState.Loading) action()
    return this
}

fun <T> UiState<T>.getOrNull(): T? {
    return if (this is UiState.Success) this.data else null
}