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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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

    private val _deckId = MutableSharedFlow<Int>(replay = 1)

    val screenUiState: StateFlow<UiState<DeckScreenData>> =
        _deckId
            .flatMapLatest { id ->
                combine(
                    getDeckByIdUseCase(id),
                    getCardsUseCase(id),
                ) { deck, cards ->
                    if (deck != null) {
                        UiState.Success(DeckScreenData(deck = deck, cards = cards))
                    } else {
                        UiState.Error(R.string.error_failed_to_load_deck)
                    }
                }.catch { e ->
                    when (e) {
                        is DomainError.DatabaseError -> emit(UiState.Error(R.string.error_failed_to_load_deck))
                        else -> emit(UiState.Error(R.string.error_something_went_wrong))
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun loadDeckWithCards(deckId: Int) {
        _deckId.tryEmit(deckId)
    }

    private val _modalState = MutableStateFlow<ModalState>(ModalState.None)
    val modalState: StateFlow<ModalState> = _modalState.asStateFlow()

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

    fun deleteDeck(deckId: Int) {
        viewModelScope.launch {
            if (!deleteDeckMutex.tryLock()) return@launch

            try {
                deleteDeckUseCase(deckId = deckId)
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
