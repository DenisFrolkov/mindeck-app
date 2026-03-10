package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.usecases.card.query.GetCardByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.toUiState
import com.mindeck.presentation.state.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CardStudyViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase,
) : ViewModel() {

    private val _modalState = MutableStateFlow<ModalState>(ModalState.None)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

    private val _cardsState = MutableStateFlow<UiState<List<Card>>>(UiState.Idle)
    val cardsState = _cardsState.asStateFlow()

    fun loadCardById(cardId: Int) {
        if (_cardsState.value is UiState.Loading) return
        viewModelScope.launch {
            _cardsState.value = UiState.Loading
            getCardByIdUseCase(cardId = cardId)
                .catch { e ->
                    _cardsState.value = if (e is DomainError) {
                        UiState.Error(e.toUserMessage())
                    } else {
                        UiState.Error(R.string.error_something_went_wrong)
                    }
                }
                .collect { card ->
                    _cardsState.value = if (card != null) {
                        UiState.Success(listOf(card))
                    } else {
                        UiState.Error(R.string.error_something_went_wrong)
                    }
                }
        }
    }

    fun loadCardRepetition() {
        if (_cardsState.value is UiState.Loading) return
        viewModelScope.launch {
            _cardsState.value = UiState.Loading
            getCardsRepetitionUseCase()
                .toUiState()
                .collect { _cardsState.value = it }
        }
    }

    fun showDropdownMenu() {
        _modalState.update { ModalState.DropdownMenu }
    }

    fun hideModal() {
        _modalState.update { ModalState.None }
    }
}
