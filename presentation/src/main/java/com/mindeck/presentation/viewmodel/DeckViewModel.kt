package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.cardUseCase.AddCardsToDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.DeleteCardsFromDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.GetAllCardsByDeckIdUseCase
import com.mindeck.domain.usecases.cardUseCase.MoveCardsBetweenDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksByFolderIdUseCase
import com.mindeck.domain.usecases.deckUseCases.GetDeckByIdUseCase
import com.mindeck.domain.usecases.deckUseCases.RenameDeckUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.viewmodel.managers.EditModeManager
import com.mindeck.presentation.viewmodel.managers.SelectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val getAllCardsByDeckIdUseCase: GetAllCardsByDeckIdUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    private val getAllDecksByFolderIdUseCase: GetAllDecksByFolderIdUseCase,
    private val renameDeckUseCase: RenameDeckUseCase,
    private val moveCardsBetweenDeckUseCase: MoveCardsBetweenDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val addCardsToDeckUseCase: AddCardsToDeckUseCase,
    private val editModeManager: EditModeManager,
    private val selectionManager: SelectionManager,
    private val deleteCardsFromDeckUseCase: DeleteCardsFromDeckUseCase,
    private val updateCardUseCase: UpdateCardUseCase
) : ViewModel() {

    private val _listCardsUiState = MutableStateFlow<UiState<List<Card>>>(UiState.Loading)
    val listCardsUiState: StateFlow<UiState<List<Card>>> = _listCardsUiState

    fun loadCardsForDeck(deckId: Int) {
        viewModelScope.launch {
            getAllCardsByDeckIdUseCase(deckId = deckId)
                .map<List<Card>, UiState<List<Card>>> {
                    UiState.Success(it)
                }
                .catch { emit(UiState.Error(it)) }
                .collect { state ->
                    _listCardsUiState.value = state
                }
        }
    }

    private val _deckUiState = MutableStateFlow<UiState<Deck>>(UiState.Loading)
    val deckUIState: StateFlow<UiState<Deck>> = _deckUiState

    fun getDeckById(deckId: Int) {
        viewModelScope.launch {
            _deckUiState.value = try {
                UiState.Success(getDeckByIdUseCase(deckId = deckId))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _listDecksUiState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val listDecksUiState: StateFlow<UiState<List<Deck>>> = _listDecksUiState

    fun getAllDecksByFolderId(folderId: Int) {
        viewModelScope.launch {
            getAllDecksByFolderIdUseCase(folderId = folderId)
                .map<List<Deck>, UiState<List<Deck>>> {
                    UiState.Success(it)
                }
                .catch { emit(UiState.Error(it)) }
                .collect { state ->
                    _listDecksUiState.value = state
                }
        }
    }

    private val _renameDeckState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val renameDeckState: StateFlow<UiState<Unit>> = _renameDeckState

    fun renameDeck(deckId: Int, newDeckName: String) {
        viewModelScope.launch {
            _renameDeckState.value = try {
                renameDeckUseCase(deckId = deckId, newName = newDeckName)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _moveCardsBetweenDecksState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val moveCardsBetweenDecksState: StateFlow<UiState<Unit>> = _moveCardsBetweenDecksState

    fun moveCardsBetweenDecks(
        deckIds: List<Int>,
        sourceDeckId: Int,
        targetDeckId: Int
    ) {
        viewModelScope.launch {
            _moveCardsBetweenDecksState.value = try {
                moveCardsBetweenDeckUseCase(deckIds, sourceDeckId, targetDeckId)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _deleteDeckState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteDeckState: StateFlow<UiState<Unit>> = _deleteDeckState

    fun deleteDeck(deck: Deck) {
        viewModelScope.launch {
            _deleteDeckState.value = try {
                deleteDeckUseCase(deck = deck)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _addDecksToFolder = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val addDecksToFolder: StateFlow<UiState<Unit>> = _addDecksToFolder

    fun addCardsToDeck(
        cardIds: List<Int>,
        targetDeckId: Int
    ) {
        viewModelScope.launch {
            _addDecksToFolder.value = try {
                addCardsToDeckUseCase(cardIds, targetDeckId)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    val isEditModeEnabled: StateFlow<Boolean> = editModeManager.isEditModeEnabled

    val selectedCardIdSet: StateFlow<Set<Int>> = selectionManager.selectedItemIds

    fun toggleCardSelection(cardId: Int) {
        selectionManager.toggleSelection(cardId)
    }

    fun toggleEditMode() {
        editModeManager.toggleEditMode()
    }

    fun clearCardSelection() {
        selectionManager.clearSelected()
        toggleEditMode()
    }
}