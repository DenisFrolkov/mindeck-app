package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStudyViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
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
}