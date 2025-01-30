package com.mindeck.presentation.viewmodel.managers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class EditModeManager @Inject constructor() {
    private val _isEditModeEnabled = MutableStateFlow(false)
    val isEditModeEnabled: StateFlow<Boolean> = _isEditModeEnabled

    fun toggleEditMode() {
        _isEditModeEnabled.update { !it }
    }
}