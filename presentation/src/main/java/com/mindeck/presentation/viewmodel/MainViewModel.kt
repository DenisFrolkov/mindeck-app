package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getAllDecksUseCase: GetAllDecksUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase,
) : ViewModel() {

    private val _decksState = MutableStateFlow<UiState<List<Deck>>>(UiState.Idle)
    val decksState: StateFlow<UiState<List<Deck>>> = _decksState.asStateFlow()

    fun loadDecks() {
        if (_decksState.value is UiState.Loading) return

        viewModelScope.launch {
            _decksState.value = UiState.Loading
            getAllDecksUseCase()
                .toUiState()
                .collect { _decksState.value = it }
        }
    }

    private val _cardsForRepetitionState = MutableStateFlow<UiState<List<Card>>>(UiState.Idle)
    val cardsForRepetitionState = _cardsForRepetitionState.asStateFlow()

    // В будущем буду применять, а пока оставлю тут
    fun loadCardRepetition(currentTime: Long) {
        if (_decksState.value is UiState.Loading) return

        viewModelScope.launch {
            _cardsForRepetitionState.value = UiState.Loading
            getCardsRepetitionUseCase(currentTime = currentTime)
                .toUiState()
                .collect { _cardsForRepetitionState.value = it }
        }
    }
}
