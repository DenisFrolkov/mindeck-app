package com.mindeck.presentation.viewmodel.managers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SelectionManager @Inject constructor() {

    private val _selectedCardIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedCardIds: StateFlow<Set<Int>> = _selectedCardIds

    private val _selectedDeckId = MutableStateFlow<Int?>(null)
    val selectedDeckId: StateFlow<Int?> = _selectedDeckId

    fun toggleCardSelection(cardId: Int) {
        _selectedCardIds.update { selected ->
            selected.toMutableSet().apply {
                if (contains(cardId)) remove(cardId) else add(cardId)
            }
        }
    }

    fun toggleDeckSelection(deckId: Int) {
        _selectedDeckId.update { selected ->
            deckId
        }
    }

    fun clearCardSelection() {
        _selectedCardIds.value = emptySet()
    }

    fun clearDeckSelection() {
        _selectedDeckId.value = null
    }
}
