package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class DecksViewModel @Inject constructor(
    getAllDecksUseCase: GetAllDecksUseCase,
) : ViewModel() {
    val decksState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .toUiState()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), UiState.Loading)
}
