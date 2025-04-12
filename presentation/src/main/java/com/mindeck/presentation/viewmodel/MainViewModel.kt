package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.cardUseCase.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deckUseCases.CreateDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getAllFoldersUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase
) : ViewModel() {

    val foldersState: StateFlow<UiState<List<Deck>>> = getAllFoldersUseCase()
        .map<List<Deck>, UiState<List<Deck>>> { UiState.Success(it) }
        .catch { emit(UiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    fun createDeck(deckName: String) {
        viewModelScope.launch {
            _createDeckState.value = try {
                createDeckUseCase(Deck(deckName = deckName))
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _cardsForRepetitionState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val cardsForRepetitionState = _cardsForRepetitionState.asStateFlow()

    fun loadCardRepetition(currentTime: Long) {
        viewModelScope.launch {
            getCardsRepetitionUseCase(currentTime = currentTime)
                .map<List<Card>, UiState<List<Card>>> {
                    UiState.Success(it)
                }
                .catch { emit(UiState.Error(it)) }
                .collect { state ->
                    _cardsForRepetitionState.value = state
                }
        }
    }
}