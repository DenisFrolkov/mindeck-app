package com.mindeck.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.command.DeleteCardUseCase
import com.mindeck.domain.usecases.card.command.UpdateCardUseCase
import com.mindeck.domain.usecases.card.query.GetCardByIdUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.viewmodel.managers.BaseViewModel
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
) : BaseViewModel() {

    private val _cardByCardIdUIState = MutableStateFlow<UiState<Card>>(UiState.Loading)
    val cardByCardIdUIState = _cardByCardIdUIState.asStateFlow()

    fun loadCardById(cardId: Int) = launchUiState(_cardByCardIdUIState) {
        val card = getCardByIdUseCase(cardId)
        viewModelScope.launch { getDeckById(card.deckId) }
        card
    }

    private val _deckUiState = MutableStateFlow<UiState<Deck>>(UiState.Loading)
    val deckUIState: StateFlow<UiState<Deck>> = _deckUiState

    fun getDeckById(deckId: Int) = launchUiState(_deckUiState) {
        getDeckByIdUseCase(deckId)
    }

    private val _updateCardState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val updateCardState: StateFlow<UiState<Unit>> = _updateCardState

    fun updateCard(card: Card) = launchUiState(_updateCardState) {
        updateCardUseCase(card)
    }

    private val _deleteCardState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val deleteCardState: StateFlow<UiState<Unit>> = _deleteCardState

    fun deleteCard(card: Card) = launchUiState(_deleteCardState) {
        deleteCardUseCase(card)
    }
}