package com.mindeck.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.cardUseCase.DeleteCardsFromDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.GetAllCardsByDeckIdUseCase
import com.mindeck.domain.usecases.cardUseCase.MoveCardsBetweenDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.AddDecksToFolderUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.GetDeckByIdUseCase
import com.mindeck.domain.usecases.deckUseCases.RenameDeckUseCase
import com.mindeck.presentation.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val getAllCardsByDeckIdUseCase: GetAllCardsByDeckIdUseCase,
    private val renameDeckUseCase: RenameDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    private val addDecksToFolderUseCase: AddDecksToFolderUseCase,
    private val deleteCardsFromDeckUseCase: DeleteCardsFromDeckUseCase,
    private val moveCardsBetweenDeckUseCase: MoveCardsBetweenDeckUseCase,
    private val updateCardUseCase: UpdateCardUseCase
) : ViewModel() {

    private val _cardUIState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val cardUIState: StateFlow<UiState<List<Card>>> = _cardUIState

    private val _deckUIState = MutableStateFlow<UiState<Deck>>(UiState.Loading)
    val deckUIState: StateFlow<UiState<Deck>> = _deckUIState

    fun renameDeck(deckId: Int, newDeckName: String) {
        viewModelScope.launch {
            renameDeckUseCase.invoke(deckId = deckId, newName = newDeckName)
        }
    }

    fun getAllCardsByDeckId(deckId: Int) {
        viewModelScope.launch {
            try {
                getAllCardsByDeckIdUseCase.invoke(deckId = deckId).collect { card ->
                    _cardUIState.value = UiState.Success(card)
                }
            } catch (e: Exception) {
                _cardUIState.value = UiState.Error(e)
                Log.e("DeckViewModel", "Error fetching card: ${e.message}")
            }
        }
    }

    fun getDeckById(deckId: Int) {
        viewModelScope.launch {
            try {
                val deck = getDeckByIdUseCase(deckId = deckId)
                _deckUIState.value = UiState.Success(deck)
            } catch (e: Exception) {
                _deckUIState.value = UiState.Error(e)
                Log.e("DeckViewModel", "Error fetching folder: ${e.message}")
            }
        }
    }


    fun deleteDeck(deck: Deck) {
        viewModelScope.launch {
            deleteDeckUseCase.invoke(deck = deck)
        }
    }
}