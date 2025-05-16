package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.deckUseCases.CreateDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mindeck.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(
    getAllDecksUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase
) : ViewModel() {

    val decksState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .map<List<Deck>, UiState<List<Deck>>> { UiState.Success(it) }
        .catch { emit(UiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    fun createDeck(deckName: String) {
        viewModelScope.launch {
            if (deckName.isBlank()) {
                _createDeckState.value = UiState.Error(Throwable("Поле ввода пустое."))
                return@launch
            }

            _createDeckState.value = UiState.Loading

            _createDeckState.value = try {
                createDeckUseCase(Deck(deckName = deckName))
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(Throwable("Колода с таким названием уже существует."))
            }
        }
    }

    var modalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleModalWindow(switch: Boolean) {
        if (!switch) {
            _createDeckState.value = UiState.Success(Unit)
        }

        modalWindowValue.value = switch
    }
}