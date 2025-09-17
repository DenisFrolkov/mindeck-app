package com.mindeck.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.command.DeleteCardsFromDeckUseCase
import com.mindeck.domain.usecases.card.command.MoveCardsBetweenDeckUseCase
import com.mindeck.domain.usecases.card.query.GetAllCardsByDeckIdUseCase
import com.mindeck.domain.usecases.deck.command.DeleteDeckUseCase
import com.mindeck.domain.usecases.deck.command.RenameDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.getOrNull
import com.mindeck.presentation.util.asUiState
import com.mindeck.presentation.viewmodel.managers.BaseViewModel
import com.mindeck.presentation.viewmodel.managers.EditModeManager
import com.mindeck.presentation.viewmodel.managers.SelectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val getAllCardsByDeckIdUseCase: GetAllCardsByDeckIdUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    getAllDecksUseCase: GetAllDecksUseCase,
    private val renameDeckUseCase: RenameDeckUseCase,
    private val moveCardsBetweenDeckUseCase: MoveCardsBetweenDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val editModeManager: EditModeManager,
    private val selectionManager: SelectionManager,
    private val deleteCardsFromDeckUseCase: DeleteCardsFromDeckUseCase,
) : BaseViewModel() {

    private val _listCardsUiState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val listCardsUiState: StateFlow<UiState<List<Card>>> = _listCardsUiState

    fun loadCardsForDeck(deckId: Int) {
        viewModelScope.launch {
            getAllCardsByDeckIdUseCase(deckId = deckId)
                .asUiState()
                .collect { state ->
                    _listCardsUiState.value = state
                }
        }
    }

    private val _deckUiState = MutableStateFlow<UiState<Deck>>(UiState.Loading)
    val deckUIState: StateFlow<UiState<Deck>> = _deckUiState

    fun getDeckById(deckId: Int) = launchUiState(_deckUiState) {
        getDeckByIdUseCase(deckId = deckId)
    }

    val listDecksUiState: StateFlow<UiState<List<Deck>>> = getAllDecksUseCase()
        .asUiState()
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    var renameModalWindowValue = MutableStateFlow<Boolean>(false)

    private val _renameDeckState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val renameDeckState: StateFlow<UiState<Unit>> = _renameDeckState

    fun toggleRenameModalWindow(switch: Boolean) {
        if (!switch) {
            _renameDeckState.value = UiState.Success(Unit)
        }

        renameModalWindowValue.value = switch
    }

    fun renameDeck(deckId: Int, newDeckName: String) {
        if (newDeckName.isBlank()) {
            _renameDeckState.value = UiState.Error(DomainError.UnknownError(Throwable("Поле ввода пустое.")))
            return
        }

        val currentDeckState = _deckUiState.value
        if (currentDeckState is UiState.Success) {
            val currentName = currentDeckState.data.deckName
            if (newDeckName == currentName) {
                _renameDeckState.value = UiState.Error(Throwable("Название совпадает с текущим."))
                return
            }
        }

        launchUiState(_renameDeckState) {
            renameDeckUseCase(deckId = deckId, newName = newDeckName)
            toggleRenameModalWindow(false)
        }
    }

    var editCardsInDeckModalWindowValue = MutableStateFlow<Boolean>(false)
    private val _moveCardsBetweenDecksState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val moveCardsBetweenDecksState: StateFlow<UiState<Unit>> = _moveCardsBetweenDecksState

    fun toggleEditCardsInDeckModalWindow(switch: Boolean) {
        if (!switch) {
            _moveCardsBetweenDecksState.value = UiState.Success(Unit)
            toggleEditMode()
            toggleMovementButton()
        }

        editCardsInDeckModalWindowValue.value = switch
    }

    fun moveCardsBetweenDecks(
        cardIds: List<Int>,
        sourceDeckId: Int?,
        targetDeckId: Int?
    ) = launchUiState(_moveCardsBetweenDecksState) {
        if (sourceDeckId != null && targetDeckId != null) {
            moveCardsBetweenDeckUseCase(cardIds, sourceDeckId, targetDeckId)
            toggleEditCardsInDeckModalWindow(false)
        } else {
            throw IllegalArgumentException("Source or target deck id is null")
        }
    }

    private val _deleteCardsBetweenDecksState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val deleteCardsBetweenDecksState: StateFlow<UiState<Unit>> = _deleteCardsBetweenDecksState

    fun deleteCardsBetweenDeck(
        cardIds: List<Int>,
        sourceDeckId: Int?,
    ) = launchUiState(_deleteCardsBetweenDecksState) {
        if (sourceDeckId != null) {
            deleteCardsFromDeckUseCase(cardIds, sourceDeckId)
        } else {
            throw IllegalArgumentException("sourceDeckId is null")
        }
    }

    private val _deleteDeckState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteDeckState: StateFlow<UiState<Unit>> = _deleteDeckState

    fun deleteDeck(deck: Deck) = launchUiState(_deleteDeckState) {
        deleteDeckUseCase(deck = deck)
    }

    var deleteDeckModalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleDeleteDeckModalWindow(switch: Boolean) {
        deleteDeckModalWindowValue.value = switch
    }

    fun deleteDeckOrIncludeModalWindow(deck: UiState<Deck>?, action: () -> Unit) {
        val deck = deck?.getOrNull()
        val cards = _listCardsUiState.value.getOrNull()

        if (cards == null || cards.isEmpty()) {
            deck?.let { deleteDeck(it) }
            action()
        } else {
            toggleDeleteDeckModalWindow(true)
        }
    }

    var deleteCardsModalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleDeleteCardsModalWindow(switch: Boolean) {
        if (!switch)
            toggleEditMode()

        deleteCardsModalWindowValue.value = switch
    }

    val isEditModeEnabled: StateFlow<Boolean> = editModeManager.isEditModeEnabled

    val selectedCardIdSet: StateFlow<Set<Int>> = selectionManager.selectedCardIds
    val selectedDeckId: StateFlow<Int?> = selectionManager.selectedDeckId

    fun toggleCardSelection(cardId: Int) {
        selectionManager.toggleCardSelection(cardId)
    }

    fun toggleDeckSelection(deckId: Int) {
        selectionManager.toggleDeckSelection(deckId)
    }

    fun toggleEditMode() {
        editModeManager.toggleEditMode()
    }

    fun toggleMovementButton() {
        if (!isEditModeEnabled.value)
            selectionManager.clearCardSelection()
    }
}