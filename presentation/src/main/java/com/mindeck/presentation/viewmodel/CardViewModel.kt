package com.mindeck.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.cardUseCase.DeleteCardUseCase
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.domain.usecases.cardUseCase.GetFolderByCardIdUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val getFolderByCardIdUseCase: GetFolderByCardIdUseCase
) : ViewModel() {

    private val _cardUIState = MutableStateFlow<UiState<Card>>(UiState.Loading)
    val cardUIState = _cardUIState.asStateFlow()

    private val _folderUIState = MutableStateFlow<UiState<Folder?>>(UiState.Loading)
    val folderUIState = _folderUIState.asStateFlow()

    fun getCardById(cardId: Int) {
        viewModelScope.launch {
            try {
                val deck = getCardByIdUseCase(cardId = cardId)
                _cardUIState.value = UiState.Success(deck)
            } catch (e: Exception) {
                _cardUIState.value = UiState.Error(e)
                Log.e("CardViewModel", "Error fetching card: ${e.message}")
            }
        }
    }

    fun getFolderByCardId(cardId: Int) {
        viewModelScope.launch {
            try {
                val folder = getFolderByCardIdUseCase(cardId = cardId)
                _folderUIState.value =
                    UiState.Success(folder)
            } catch (e: Exception) {
                _folderUIState.value = UiState.Error(e)
                Log.e("CardViewModel", "Error fetching folder by cardId: ${e.message}")
            }
        }
    }
}