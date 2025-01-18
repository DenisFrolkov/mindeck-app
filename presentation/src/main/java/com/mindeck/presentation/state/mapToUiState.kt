package com.mindeck.presentation.state

fun <T, R> UiState<List<T>>.mapToUiState(transform: (List<T>) -> List<R>): UiState<List<R>> {
    return when (this) {
        is UiState.Success -> UiState.Success(transform(data))
        is UiState.Error -> UiState.Error(exception)
        else -> UiState.Loading
    }
}