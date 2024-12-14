package com.mindeck.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.cardUseCase.DeleteCardUseCase
import com.mindeck.domain.usecases.cardUseCase.GetAllCardsByDeckIdUseCase
import com.mindeck.domain.usecases.deckUseCases.CreateDeckUseCase
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
    private val createDeckUseCase: CreateDeckUseCase,
    private val renameDeckUseCase: RenameDeckUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
) : ViewModel() {

    private val _cardUIState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val cardUIState: StateFlow<UiState<List<Card>>> = _cardUIState

    init {
//        createDeck(Deck(deckName = "12", folderId = 1))
    }

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

    fun createDeck(deck: Deck) {
        viewModelScope.launch {
            createDeckUseCase.invoke(deck = deck)
        }
    }


    fun deleteCard(card: Card) {
        viewModelScope.launch {
            deleteCardUseCase.invoke(card = card)
        }
    }
}