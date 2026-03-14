package com.mindeck.presentation.viewmodel.managers

import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.toUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex

class DeckSelectionHandler(
    getAllDecksUseCase: GetAllDecksUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
    scope: CoroutineScope,
) {
    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    private val createDeckMutex = Mutex()

    val decksState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .toUiState()
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    suspend fun createDeck(deckName: String): Int? {
        if (!createDeckMutex.tryLock()) return null

        try {
            _createDeckState.value = UiState.Loading
            val deckId = createDeckUseCase(deck = Deck(deckName = deckName))
            _createDeckState.value = UiState.Idle
            return deckId
        } catch (e: DomainError.NameAlreadyExists) {
            _createDeckState.value = UiState.Error(R.string.error_deck_name_taken)
        } catch (e: DomainError.DatabaseError) {
            _createDeckState.value = UiState.Error(R.string.error_failed_to_create_deck)
        } catch (e: DomainError) {
            _createDeckState.value = UiState.Error(R.string.error_something_went_wrong)
        } catch (e: Exception) {
            _createDeckState.value = UiState.Error(R.string.error_something_went_wrong)
        } finally {
            createDeckMutex.unlock()
        }
        return null
    }

    fun resetCreateDeckError() {
        if (_createDeckState.value is UiState.Error) {
            _createDeckState.value = UiState.Idle
        }
    }
}
