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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    getAllDecksUseCase: GetAllDecksUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase,
) : ViewModel() {

    val decksState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .toUiState()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), UiState.Loading)

    private val _cardsForRepetitionState = MutableStateFlow<UiState<List<Card>>>(UiState.Idle)
    val cardsForRepetitionState = _cardsForRepetitionState.asStateFlow()

    // В будущем буду применять, а пока оставлю тут
    fun loadCardRepetition(currentTime: Long) {
        if (_cardsForRepetitionState.value is UiState.Loading) return

        viewModelScope.launch {
            _cardsForRepetitionState.value = UiState.Loading
            getCardsRepetitionUseCase(currentTime = currentTime)
                .toUiState()
                .collect { _cardsForRepetitionState.value = it }
        }
    }
}
