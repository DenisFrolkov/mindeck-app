package com.mindeck.presentation.state

import androidx.annotation.StringRes

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(
        @StringRes val messageRes: Int,
        val args: List<Any> = emptyList(),
    ) : UiState<Nothing>()
}
