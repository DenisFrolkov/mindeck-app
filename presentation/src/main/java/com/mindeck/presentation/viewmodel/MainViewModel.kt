package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainEvent {
    class OnLoadDecks() : MainEvent
    class OnCreateDeckClick(val deckName: String) : MainEvent
    class OnLoadCardRepetition(val currentTime: Long) : MainEvent
}

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase,
) : ViewModel() {

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnLoadDecks -> loadDecks()
            is MainEvent.OnCreateDeckClick -> createDeck(event.deckName)
            is MainEvent.OnLoadCardRepetition -> loadCardRepetition(event.currentTime)
        }
    }

    private val _decksState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val decksState: StateFlow<UiState<List<Deck>>> = _decksState

    private fun loadDecks() {
        viewModelScope.launch {
            getAllFoldersUseCase()
                .map { UiState.Success(it) }
                .catch { _decksState.value = UiState.Error(it.message ?: "") }
                .collect { _decksState.value = it }
        }
    }

    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    fun createDeck(deckName: String) {
        viewModelScope.launch {
            _createDeckState.value = UiState.Loading

            _createDeckState.value = try {
                createDeckUseCase(Deck(deckName = deckName))
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(NAME_EXISTS)
            }
        }
    }

    private val _cardsForRepetitionState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val cardsForRepetitionState = _cardsForRepetitionState.asStateFlow()

    fun loadCardRepetition(currentTime: Long) {
        viewModelScope.launch {
            getCardsRepetitionUseCase(currentTime = currentTime)
                .map { UiState.Success(it) }
                .catch { UiState.Error(it.message ?: "") }
                .collect { _cardsForRepetitionState.value = it }
        }
    }
}

const val NAME_EXISTS = "Колода с таким названием уже существует."
