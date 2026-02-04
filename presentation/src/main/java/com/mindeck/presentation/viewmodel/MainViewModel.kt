package com.mindeck.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.utils.stringToMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(
    getAllFoldersUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase,
) : ViewModel() {

    init {
        Log.d("MainViewModel", "created")
    }

    val currentDateTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    fun initRepetition() {
        val millis = stringToMillis(currentDateTime)
        loadCardRepetition(millis)
    }

    val decksState: StateFlow<UiState<List<Deck>>> = getAllFoldersUseCase()
        .map<List<Deck>, UiState<List<Deck>>> { UiState.Success(it) }
        .catch { emit(UiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    fun createDeck(deckName: String) {
        viewModelScope.launch {
            _createDeckState.value = UiState.Loading

            if (deckName.isBlank()) {
                _createDeckState.value = UiState.Error(Throwable("Поле ввода пустое."))
                return@launch
            }

            _createDeckState.value = try {
                createDeckUseCase(Deck(deckName = deckName))
                toggleModalWindow(false)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(Throwable("Колода с таким названием уже существует."))
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

    var createModalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleModalWindow(switch: Boolean) {
        if (!switch) {
            _createDeckState.value = UiState.Success(Unit)
        }

        createModalWindowValue.value = switch
    }
}
