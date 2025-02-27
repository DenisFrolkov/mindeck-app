package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.domain.usecases.cardUseCase.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardReviewUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStudyViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val updateCardReviewUseCase: UpdateCardReviewUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase
) : ViewModel() {
    private val _cardByCardIdUIState = MutableStateFlow<UiState<Card>>(UiState.Loading)
    val cardByCardIdUIState = _cardByCardIdUIState.asStateFlow()

    fun loadCardById(cardId: Int) {
        viewModelScope.launch {
            _cardByCardIdUIState.value = try {
                UiState.Success(getCardByIdUseCase(cardId = cardId))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _updateCardReviewState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val updateCardReviewState: StateFlow<UiState<Unit>> = _updateCardReviewState

    fun updateReview(
        cardId: Int,
        firstReviewDate: Long?,
        repetitionCount: Int,
        lastReviewType: ReviewType
    ) {
        viewModelScope.launch {
            _updateCardReviewState.value = try {
                updateCardReviewUseCase(
                    cardId, firstReviewDate, repetitionCount, lastReviewType
                )
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