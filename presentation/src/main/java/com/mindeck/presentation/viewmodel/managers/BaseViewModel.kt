package com.mindeck.presentation.viewmodel.managers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    protected fun <T> launchUiState(
        state: MutableStateFlow<UiState<T>>,
        block: suspend () -> T
    ) {
        viewModelScope.launch {
            state.value = UiState.Loading
            state.value = try {
                UiState.Success(block())
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}