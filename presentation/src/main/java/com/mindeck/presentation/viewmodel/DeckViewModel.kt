package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.query.GetAllCardsUseCase
import com.mindeck.domain.usecases.deck.command.DeleteDeckUseCase
import com.mindeck.domain.usecases.deck.command.RenameDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckViewModel
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
                _screenUiState.value = UiState.Error("Failed to load deck")
            } catch (e: DomainError) {
                _screenUiState.value = UiState.Error("Something went wrong")
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
                _navigationEvent.send(DeckNavigationEvent.CloseRenameWindow)
            } catch (e: DomainError.NameAlreadyExists) {
                _renameDeckState.value = UiState.Error("Deck '$newDeckName' already exists")
            } catch (e: DomainError.DatabaseError) {
                _renameDeckState.value = UiState.Error("Database error, try again")
            } catch (e: DomainError.UnknownError) {
                _renameDeckState.value = UiState.Error("Something went wrong")
            }
        }
    }

    fun resetRenameDeckState() {
        _renameDeckState.value = UiState.Idle
    }

    private var deletionJob: Job? = null

    fun deleteDeck(deck: Deck) {
        if (deletionJob?.isActive == true) return

        deletionJob =
            viewModelScope.launch {
                try {
                    deleteDeckUseCase(deck = deck)

                    _navigationEvent.send(DeckNavigationEvent.GoBack)
                    _navigationEvent.send(DeckNavigationEvent.ShowToast("Deck deleted successfully"))
                } catch (e: DomainError) {
                    _navigationEvent.send(DeckNavigationEvent.ShowToast("Failed to delete deck"))
                } finally {
                    deletionJob = null
                }
            }
    }
}

data class DeckScreenData(
    val deck: Deck,
    val cards: List<Card>,
)

sealed interface DeckNavigationEvent {
    data object GoBack : DeckNavigationEvent

    data object CloseRenameWindow : DeckNavigationEvent

    data class ShowToast(
        val message: String,
    ) : DeckNavigationEvent
}
