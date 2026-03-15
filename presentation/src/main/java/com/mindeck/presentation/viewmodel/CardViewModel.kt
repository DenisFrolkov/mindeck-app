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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class CardViewModel @Inject constructor(
    private val getCardWithDeckByIdUseCase: GetCardWithDeckByIdUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
) : ViewModel() {

    private val _uiEvent = Channel<CardUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _cardId = MutableSharedFlow<Int>(replay = 1)

    val cardWithDeck: StateFlow<UiState<CardWithDeck>> =
        _cardId
            .flatMapLatest { id ->
                getCardWithDeckByIdUseCase(cardId = id)
                    .map { cardWithDeck ->
                        if (cardWithDeck != null) {
                            UiState.Success(cardWithDeck)
                        } else {
                            UiState.Error(R.string.error_failed_to_load_card)
                        }
                    }
                    .catch { e ->
                        when (e) {
                            is DomainError.DatabaseError -> emit(UiState.Error(R.string.error_failed_to_load_card))
                            else -> emit(UiState.Error(R.string.error_something_went_wrong))
                        }
                    }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun loadCardById(cardId: Int) {
        viewModelScope.launch {
            _cardId.emit(cardId)
        }
    }

    private val _deleteCardState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteCardState: StateFlow<UiState<Unit>> = _deleteCardState.asStateFlow()

    private val _modalState = MutableStateFlow<ModalState>(ModalState.None)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

    private val deleteCardMutex = Mutex()

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            if (!deleteCardMutex.tryLock()) return@launch

            try {
                _deleteCardState.update { UiState.Loading }
                deleteCardUseCase(cardId = card.cardId)
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
