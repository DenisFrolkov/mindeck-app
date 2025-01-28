package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.cardUseCase.AddCardsToDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.DeleteCardsFromDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.GetAllCardsByDeckIdUseCase
import com.mindeck.domain.usecases.cardUseCase.MoveCardsBetweenDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksByFolderIdUseCase
import com.mindeck.domain.usecases.deckUseCases.GetDeckByIdUseCase
import com.mindeck.domain.usecases.deckUseCases.RenameDeckUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val getAllDecksByFolderIdUseCase: GetAllDecksByFolderIdUseCase,
    private val getAllCardsByDeckIdUseCase: GetAllCardsByDeckIdUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    private val moveCardsBetweenDeckUseCase: MoveCardsBetweenDeckUseCase,
    private val renameDeckUseCase: RenameDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val addCardsToDeckUseCase: AddCardsToDeckUseCase,
    private val deleteCardsFromDeckUseCase: DeleteCardsFromDeckUseCase,
    private val updateCardUseCase: UpdateCardUseCase
) : ViewModel() {

    private val _listCardsUiState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val listCardsUiState: StateFlow<UiState<List<Card>>> = _listCardsUiState

    private val _deckUiState = MutableStateFlow<UiState<Deck>>(UiState.Loading)
    val deckUIState: StateFlow<UiState<Deck>> = _deckUiState

    private val _listDecksUiState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val listDecksUiState: StateFlow<UiState<List<Deck>>> = _listDecksUiState

    private val _listSelectedCards = MutableStateFlow<Set<Int>>(emptySet())
    val listSelectedCards: StateFlow<Set<Int>> = _listSelectedCards

    private val _isEditModeEnabled = MutableStateFlow(false)
    val isEditModeEnabled: StateFlow<Boolean> = _isEditModeEnabled.asStateFlow()

    fun getAllDecksByFolderId(folderId: Int) {
        viewModelScope.launch {
            try {
                getAllDecksByFolderIdUseCase(folderId = folderId).collect { decks ->
                    _listDecksUiState.value = UiState.Success(decks)
                }
            } catch (e: Exception) {
                _listDecksUiState.value = UiState.Error(e)
            }
        }
    }

    fun getAllCardsByDeckId(deckId: Int) {
        viewModelScope.launch {
            try {
                getAllCardsByDeckIdUseCase.invoke(deckId = deckId).collect { card ->
                    _listCardsUiState.value = UiState.Success(card)
                }
            } catch (e: Exception) {
                _listCardsUiState.value = UiState.Error(e)
            }
        }
    }

    fun getDeckById(deckId: Int) {
        viewModelScope.launch {
            try {
                val deck = getDeckByIdUseCase(deckId = deckId)
                _deckUiState.value = UiState.Success(deck)
            } catch (e: Exception) {
                _deckUiState.value = UiState.Error(e)
            }
        }
    }

    fun toggleCardSelection(cardId: Int) {
        _listSelectedCards.value = _listSelectedCards.value.toMutableSet().apply {
            if (contains(cardId)) {
                remove(cardId)
            } else {
                add(cardId)
            }
        }
    }

    fun moveCardsBetweenDecks(
        deckIds: List<Int>,
        sourceDeckId: Int,
        targetDeckId: Int
    ) {
        viewModelScope.launch {
            moveCardsBetweenDeckUseCase.invoke(deckIds, sourceDeckId, targetDeckId)
        }
    }

    fun addCardsToDeck(
        cardIds: List<Int>,
        targetDeckId: Int
    ) {
        viewModelScope.launch {
            addCardsToDeckUseCase.invoke(cardIds, targetDeckId)
        }
    }

    fun updateEditMode() {
        _isEditModeEnabled.value = !_isEditModeEnabled.value
    }

    fun clearSelection() {
        _listSelectedCards.value = emptySet()
        updateEditMode()
    }

    fun renameDeck(deckId: Int, newDeckName: String) {
        viewModelScope.launch {
            renameDeckUseCase.invoke(deckId = deckId, newName = newDeckName)
        }
    }

    fun deleteDeck(deck: Deck) {
        viewModelScope.launch {
            deleteDeckUseCase.invoke(deck = deck)
        }
    }
}