package com.mindeck.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.command.CreateCardUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import com.mindeck.presentation.state.CardState
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreationCardViewModel @Inject constructor(
    private val createCardUseCase: CreateCardUseCase,
    private val getAllDecksUseCase: GetAllDecksUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase
) : ViewModel() {
    private var _cardState = mutableStateOf(
        CardState("", "", "", "", -1)
    )
    val cardState: State<CardState> = _cardState

    private val _validation = MutableStateFlow<Boolean?>(null)
    val validation: StateFlow<Boolean?> = _validation.asStateFlow()

    val selectedDeckForCreatingCard = mutableStateOf<Pair<String, Int?>>(Pair("Выберите колоду", null))
    val selectedTypeForCreatingCard = mutableStateOf<Pair<String, Int?>>(Pair("Выберите тип", null))

    private val _listDecksUiState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val listDecksUiState: StateFlow<UiState<List<Deck>>> = _listDecksUiState

    fun getAllDecks() {
        viewModelScope.launch {
            getAllDecksUseCase()
                .map<List<Deck>, UiState<List<Deck>>> {
                    UiState.Success(it)
                }
                .catch { emit(UiState.Error(it)) }
                .collect { state ->
                    _listDecksUiState.value = state
                }
        }
    }

    private val _createCardState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val createCardState: StateFlow<UiState<Unit>> = _createCardState

    fun createCard(
        cardName: String,
        cardQuestion: String,
        cardAnswer: String,
        cardType: String,
        cardTag: String,
        deckId: Int
    ) {
        viewModelScope.launch {
            _createCardState.value = try {
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
                UiState.Success(Unit)
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
                val deck = getDeckByIdUseCase(deckId = deckId)
                selectedDeckForCreatingCard.value = Pair(deck.deckName, deck.deckId)
                UiState.Success(deck)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
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