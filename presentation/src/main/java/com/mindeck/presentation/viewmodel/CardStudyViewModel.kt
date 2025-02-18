package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardReviewUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStudyViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val updateCardReviewUseCase: UpdateCardReviewUseCase
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
        card: Card
    ) {
        viewModelScope.launch {
            _updateCardReviewState.value = try {
                updateCardReviewUseCase(
                    card
                )
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}