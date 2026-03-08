package com.mindeck.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.query.GetAllCardsUseCase
import com.mindeck.domain.usecases.deck.command.DeleteDeckUseCase
import com.mindeck.domain.usecases.deck.command.RenameDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
internal class DeckViewModel
@Inject
constructor(
    private val getCardsUseCase: GetAllCardsUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    private val renameDeckUseCase: RenameDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
) : ViewModel() {
    private val _navigationEvent = Channel<DeckNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _screenUiState = MutableStateFlow<UiState<DeckScreenData>>(UiState.Idle)
    val screenUiState: StateFlow<UiState<DeckScreenData>> = _screenUiState.asStateFlow()

    private val _modalState = MutableStateFlow<ModalState>(ModalState.None)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

    fun loadDeckWithCards(deckId: Int) {
        if (_screenUiState.value is UiState.Loading) return

        viewModelScope.launch {
            _screenUiState.value = UiState.Loading

            try {
                val (deck, cards) =
                    coroutineScope {
                        val deferredDeck = async { getDeckByIdUseCase(deckId) }
                        val deferredCards = async { getCardsUseCase(deckId).first() }
                        deferredDeck.await() to deferredCards.await()
                    }

                _screenUiState.value =
                    UiState.Success(
                        DeckScreenData(deck = deck, cards = cards),
                    )
            } catch (e: DomainError.DatabaseError) {
                _screenUiState.value = UiState.Error(R.string.error_failed_to_load_deck)
            } catch (e: DomainError) {
                _screenUiState.value = UiState.Error(R.string.error_something_went_wrong)
            } catch (e: Exception) {
                _screenUiState.value = UiState.Error(R.string.error_something_went_wrong)
            }
        }
    }

    private val _renameDeckState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val renameDeckState: StateFlow<UiState<Unit>> = _renameDeckState.asStateFlow()

    fun renameDeck(
        deckId: Int,
        newDeckName: String,
    ) {
        if (_renameDeckState.value is UiState.Loading) return

        viewModelScope.launch {
            _renameDeckState.value = UiState.Loading

            try {
                renameDeckUseCase(deckId = deckId, newName = newDeckName)

                loadDeckWithCards(deckId)
                _renameDeckState.value = UiState.Success(Unit)
                hideModal()
            } catch (e: DomainError.NameAlreadyExists) {
                _renameDeckState.value = UiState.Error(R.string.error_deck_name_already_exists, listOf(newDeckName))
            } catch (e: DomainError.DatabaseError) {
                _renameDeckState.value = UiState.Error(R.string.error_database_try_again)
            } catch (e: DomainError) {
                _renameDeckState.value = UiState.Error(R.string.error_something_went_wrong)
            } catch (e: Exception) {
                _renameDeckState.value = UiState.Error(R.string.error_something_went_wrong)
            }
        }
    }

    fun resetRenameDeckState() {
        _renameDeckState.value = UiState.Idle
    }

    private val deleteDeckMutex = Mutex()

    fun deleteDeck(deck: Deck) {
        viewModelScope.launch {
            if (!deleteDeckMutex.tryLock()) return@launch

            try {
                deleteDeckUseCase(deck = deck)
                _navigationEvent.send(DeckNavigationEvent.GoBack)
                _navigationEvent.send(DeckNavigationEvent.ShowToast(R.string.toast_deck_deleted_successfully))
                hideModal()
            } catch (e: DomainError) {
                _navigationEvent.send(DeckNavigationEvent.ShowToast(R.string.toast_message_impossible_delete_deck))
            } catch (e: Exception) {
                _navigationEvent.send(DeckNavigationEvent.ShowToast(R.string.error_something_went_wrong))
            } finally {
                deleteDeckMutex.unlock()
            }
        }
    }

    fun showDropdownMenu() {
        _modalState.update { ModalState.DropdownMenu }
    }

    fun showRenameDialog() {
        resetRenameDeckState()
        _modalState.update { ModalState.RenameDialog }
    }

    fun hideModal() {
        _modalState.update { ModalState.None }
    }
}

data class DeckScreenData(
    val deck: Deck,
    val cards: List<Card>,
)

sealed interface DeckNavigationEvent {
    data object GoBack : DeckNavigationEvent

    data class ShowToast(
        @StringRes val messageRes: Int,
    ) : DeckNavigationEvent
}
