package com.mindeck.presentation.viewmodel.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.toUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class DecksViewModel(
    getAllDecksUseCase: GetAllDecksUseCase,
) : ViewModel() {
    val decksState: StateFlow<UiState<List<Deck>>> =
        getAllDecksUseCase()
            .toUiState()
            .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), UiState.Loading)
}
