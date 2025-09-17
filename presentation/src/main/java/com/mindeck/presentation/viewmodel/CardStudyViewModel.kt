package com.mindeck.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import com.mindeck.domain.usecases.card.command.UpdateCardReviewUseCase
import com.mindeck.domain.usecases.card.query.GetCardByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.util.asUiState
import com.mindeck.presentation.viewmodel.managers.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStudyViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val updateCardReviewUseCase: UpdateCardReviewUseCase,
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase
) : BaseViewModel() {
    private val _cardByCardIdUIState = MutableStateFlow<UiState<Card>>(UiState.Loading)
    val cardByCardIdUIState = _cardByCardIdUIState.asStateFlow()

    fun loadCardById(cardId: Int) = launchUiState(_cardByCardIdUIState) {
        getCardByIdUseCase(cardId)
    }

    private val _updateCardReviewState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val updateCardReviewState: StateFlow<UiState<Unit>> = _updateCardReviewState

    fun updateReview(
        cardId: Int,
        firstReviewDate: Long?,
        repetitionCount: Int,
        lastReviewType: ReviewType
    ) = launchUiState(_updateCardReviewState) {
        updateCardReviewUseCase(cardId, firstReviewDate, repetitionCount, lastReviewType)
    }

    private val _cardsForRepetitionState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val cardsForRepetitionState = _cardsForRepetitionState.asStateFlow()

    fun loadCardRepetition(currentTime: Long) {
        viewModelScope.launch {
            getCardsRepetitionUseCase(currentTime = currentTime)
                .asUiState()
                .collect { state ->
                    _cardsForRepetitionState.value = state
                }
        }
    }
}