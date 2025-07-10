package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.cardUseCase.DeleteCardUseCase
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.GetDeckByIdUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
) : ViewModel() {

    private val _cardByCardIdUIState = MutableStateFlow<UiState<Card>>(UiState.Loading)
    val cardByCardIdUIState = _cardByCardIdUIState.asStateFlow()

    fun loadCardById(cardId: Int) {
        viewModelScope.launch {
            _cardByCardIdUIState.value = try {
                val card = getCardByIdUseCase(cardId = cardId)
                getDeckById(card.deckId)
                UiState.Success(card)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _deckUiState = MutableStateFlow<UiState<Deck>>(UiState.Loading)
    val deckUIState: StateFlow<UiState<Deck>> = _deckUiState

    fun getDeckById(deckId: Int) {
        viewModelScope.launch {
            _deckUiState.value = try {
                UiState.Success(getDeckByIdUseCase(deckId = deckId))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _deleteCardState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteCardState: StateFlow<UiState<Unit>> = _deleteCardState

    fun deleteDeck(card: Card) {
        viewModelScope.launch {
            _deleteCardState.value = try {
                deleteCardUseCase(card = card)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}