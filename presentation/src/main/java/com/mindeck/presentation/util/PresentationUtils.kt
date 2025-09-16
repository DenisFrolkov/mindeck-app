package com.mindeck.presentation.util

import com.mindeck.presentation.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asUiState(): Flow<UiState<T>> =
    this
        .map<T, UiState<T>> { UiState.Success(it) }
        .onStart { emit(UiState.Loading) }
        .catch { e -> emit(UiState.Error(e)) }