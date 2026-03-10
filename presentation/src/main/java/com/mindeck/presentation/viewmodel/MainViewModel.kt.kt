package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.SessionSummary
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    getAllDecksUseCase: GetAllDecksUseCase,
    getCardsRepetitionUseCase: GetCardsRepetitionUseCase,
) : ViewModel() {

    val decksState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .toUiState()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), UiState.Loading)

    // Реактивные счётчики сессии: обновляются автоматически при изменении БД
    val sessionSummaryState: StateFlow<UiState<SessionSummary>> = getCardsRepetitionUseCase()
        .map { cards ->
            SessionSummary(
                newCount = cards.count { it.cardState == CardState.NEW },
                // LEARNING и LAPSE объединяем — пользователю это одна фаза
                learningCount = cards.count {
                    it.cardState == CardState.LEARNING || it.cardState == CardState.LAPSE
                },
                reviewCount = cards.count { it.cardState == CardState.REVIEW },
            )
        }
        .toUiState()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), UiState.Loading)
}
