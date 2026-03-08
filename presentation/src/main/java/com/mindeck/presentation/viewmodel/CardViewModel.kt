package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.usecases.card.command.DeleteCardUseCase
import com.mindeck.domain.usecases.card.query.GetCardWithDeckByIdUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
internal class CardViewModel @Inject constructor(
    private val getCardWithDeckByIdUseCase: GetCardWithDeckByIdUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
) : ViewModel() {

    private val _uiEvent = Channel<CardUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _cardWithDeck = MutableStateFlow<UiState<CardWithDeck>>(UiState.Idle)
    val cardWithDeck: StateFlow<UiState<CardWithDeck>> = _cardWithDeck.asStateFlow()

    private val _deleteCardState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteCardState: StateFlow<UiState<Unit>> = _deleteCardState.asStateFlow()

    private val _modalState = MutableStateFlow<ModalState>(ModalState.None)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

    private val deleteCardMutex = Mutex()

    fun loadCardById(cardId: Int) {
        if (_cardWithDeck.value is UiState.Loading) return
        viewModelScope.launch {
            _cardWithDeck.update { UiState.Loading }

            try {
                val cardWithDeck = getCardWithDeckByIdUseCase(cardId = cardId)
                _cardWithDeck.update { UiState.Success(cardWithDeck) }
            } catch (e: DomainError.DatabaseError) {
                _cardWithDeck.update { UiState.Error(R.string.error_failed_to_load_card) }
            } catch (e: DomainError) {
                _cardWithDeck.update { UiState.Error(R.string.error_something_went_wrong) }
            } catch (e: Exception) {
                _cardWithDeck.update { UiState.Error(R.string.error_something_went_wrong) }
            }
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            if (!deleteCardMutex.tryLock()) return@launch

            try {
                _deleteCardState.update { UiState.Loading }
                deleteCardUseCase(card = card)
                _deleteCardState.update { UiState.Success(Unit) }
                hideModal()
                _uiEvent.send(CardUiEvent.DeletionSuccessful(card.cardName))
            } catch (e: DomainError.DatabaseError) {
                _deleteCardState.update { UiState.Error(R.string.error_failed_to_delete_card) }
            } catch (e: DomainError) {
                _deleteCardState.update { UiState.Error(R.string.error_something_went_wrong) }
            } catch (e: Exception) {
                _deleteCardState.update { UiState.Error(R.string.error_something_went_wrong) }
            } finally {
                deleteCardMutex.unlock()
            }
        }
    }

    fun showDropdownMenu() {
        _modalState.update { ModalState.DropdownMenu }
    }

    fun showDeleteDialog() {
        _deleteCardState.update { UiState.Idle }
        _modalState.update { ModalState.DeleteDialog }
    }

    fun hideModal() {
        _modalState.update { ModalState.None }
    }
}

sealed interface CardUiEvent {
    data class DeletionSuccessful(
        val cardName: String,
    ) : CardUiEvent
}
