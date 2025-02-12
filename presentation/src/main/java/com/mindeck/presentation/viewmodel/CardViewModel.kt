package com.mindeck.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.cardUseCase.DeleteCardUseCase
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.domain.usecases.cardUseCase.GetFolderByCardIdUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val getFolderByCardIdUseCase: GetFolderByCardIdUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
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

    private val _folderUIState = MutableStateFlow<UiState<Folder?>>(UiState.Loading)
    val folderUIState = _folderUIState.asStateFlow()

    fun getFolderByCardId(cardId: Int) {
        viewModelScope.launch {
            _folderUIState.value = try {
                UiState.Success(getFolderByCardIdUseCase(cardId = cardId))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _deleteCardState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteCardState: StateFlow<UiState<Unit>> = _deleteCardState

    fun deleteDeck(card: Card) {
        viewModelScope.launch {
            _deleteCardState.value = try {
//                deleteCardUseCase(card = card)
//                UiState.Success(Unit)
                UiState.Error(Throwable())
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}