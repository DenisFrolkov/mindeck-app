package com.mindeck.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.command.CreateCardUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import com.mindeck.presentation.state.CardState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.util.asUiState
import com.mindeck.presentation.viewmodel.managers.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CreationCardViewModel @Inject constructor(
    private val createCardUseCase: CreateCardUseCase,
     getAllDecksUseCase: GetAllDecksUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase
) : BaseViewModel() {
    private var _cardState = mutableStateOf(
        CardState("", "", "", "", -1)
    )
    val cardState: State<CardState> = _cardState

    private val _validation = MutableStateFlow<Boolean?>(null)
    val validation: StateFlow<Boolean?> = _validation.asStateFlow()

    val selectedDeckForCreatingCard =
        mutableStateOf<Pair<String, Int?>>(Pair("Выберите колоду", null))
    val selectedTypeForCreatingCard = mutableStateOf<Pair<String, Int?>>(Pair("Выберите тип", null))

    val listDecksUiState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .asUiState()
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    private val _createCardState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val createCardState: StateFlow<UiState<Unit>> = _createCardState

    fun createCard(
        cardName: String,
        cardQuestion: String,
        cardAnswer: String,
        cardType: String,
        cardTag: String,
        deckId: Int
    ) = launchUiState(_createCardState) {
        createCardUseCase(
            card = Card(
                cardName = cardName,
                cardQuestion = cardQuestion,
                cardAnswer = cardAnswer,
                cardType = cardType,
                cardTag = cardTag,
                deckId = deckId
            )
        )
    }

    private val _deckUiState = MutableStateFlow<UiState<Deck>>(UiState.Loading)
    val deckUIState: StateFlow<UiState<Deck>> = _deckUiState

    fun getDeckById(deckId: Int) = launchUiState(_deckUiState) {
        val deck = getDeckByIdUseCase(deckId = deckId)
        selectedDeckForCreatingCard.value = Pair(deck.deckName, deck.deckId)
        deck
    }

    fun updateCardState(update: CardState.() -> CardState) {
        _cardState.value = _cardState.value.update()
    }

    fun validateInput(): Boolean {
        val isValid = cardState.value.title.isNotBlank() &&
                cardState.value.question.isNotBlank() &&
                cardState.value.answer.isNotBlank() &&
                selectedDeckForCreatingCard.value.second != null &&
                selectedTypeForCreatingCard.value.second != null

        _validation.value = isValid
        return isValid
    }
}