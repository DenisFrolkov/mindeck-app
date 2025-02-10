package com.mindeck.presentation.viewmodel.managers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SelectionManager @Inject constructor() {
    private val _selectedItemIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItemIds: StateFlow<Set<Int>> = _selectedItemIds

    fun toggleSelection(deckId: Int) {
        _selectedItemIds.update { selectedDeckIds ->
            selectedDeckIds.toMutableSet().apply {
                if (contains(deckId)) {
                    remove(deckId)
                } else {
                    add(deckId)
                }
            }
        }
    }

    fun clearSelected() {
        _selectedItemIds.value = emptySet()
    }
}