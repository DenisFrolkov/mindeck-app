package com.mindeck.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.util.asUiState
import com.mindeck.presentation.viewmodel.managers.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(
    getAllDecksUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase
) : BaseViewModel() {

    val decksState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .asUiState()
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    var createModalWindowValue = MutableStateFlow(false)
    private val _createDeckResult = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val createDeckResult: StateFlow<UiState<Unit>> = _createDeckResult

    fun toggleCreateModalWindow(switch: Boolean) {
        createModalWindowValue.value = switch
        if (!switch) {
            _createDeckResult.value = UiState.Success(Unit)
        }
    }

    fun createDeck(deckName: String) {
        if (deckName.isBlank()) {
            _createDeckResult.value = UiState.Error(DomainError.UnknownError(Throwable("Поле ввода пустое.")))
            return
        }

        viewModelScope.launch {
            _createDeckResult.value = UiState.Loading
            _createDeckResult.value = try {
                createDeckUseCase(Deck(deckName = deckName))
                toggleCreateModalWindow(false)
                UiState.Success(Unit)
            } catch (e: DomainError) {
                UiState.Error(e)
            }
        }
    }
}