package com.mindeck.presentation.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.toUiState(): Flow<UiState<T>> = flow {
    try {
        collect { value ->
            emit(UiState.Success(value))
        }
    } catch (e: Exception) {
        emit(UiState.Error(e.message ?: "Unknown error"))
    }
}
