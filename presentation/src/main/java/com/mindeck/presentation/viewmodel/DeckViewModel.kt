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
import com.mindeck.domain.usecases.deckUseCases.CreateDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksUseCase
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
    private val createDeckUseCase: CreateDeckUseCase,
    private val getDeckByIdUseCase: GetDeckByIdUseCase,
    private val getAllDecksUseCase: GetAllDecksUseCase,
    private val renameDeckUseCase: RenameDeckUseCase,
    private val moveCardsBetweenDeckUseCase: MoveCardsBetweenDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val addCardsToDeckUseCase: AddCardsToDeckUseCase,
    private val editModeManager: EditModeManager,
    private val selectionManager: SelectionManager,
    private val deleteCardsFromDeckUseCase: DeleteCardsFromDeckUseCase,
    private val updateCardUseCase: UpdateCardUseCase
) : ViewModel() {

    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    fun createDeck(deckName: String) {
        viewModelScope.launch {
            _createDeckState.value = UiState.Loading

            if (deckName.isBlank()) {
                _createDeckState.value = UiState.Error(Throwable("Поле ввода пустое."))
                return@launch
            }

            _createDeckState.value = try {
                createDeckUseCase(Deck(deckName = deckName))
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(Throwable("Колода с таким названием уже существует."))
            }
        }
    }

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

    fun getAllDecks() {
        viewModelScope.launch {
            getAllDecksUseCase()
                .map<List<Deck>, UiState<List<Deck>>> {
                    UiState.Success(it)
                }
                .catch { emit(UiState.Error(it)) }
                .collect { state ->
                    _listDecksUiState.value = state
                }
        }
    }

    private val _renameDeckState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val renameDeckState: StateFlow<UiState<Unit>> = _renameDeckState

    fun renameDeck(deckId: Int, newDeckName: String) {
        viewModelScope.launch {
            if (newDeckName.isBlank()) {
                _renameDeckState.value = UiState.Error(Throwable("Поле ввода пустое."))
                return@launch
            }

            val currentDeckState = _deckUiState.value
            if (currentDeckState is UiState.Success) {
                val currentName = currentDeckState.data.deckName
                if (newDeckName == currentName) {
                    _renameDeckState.value =
                        UiState.Error(Throwable("Название совпадает с текущим."))
                    return@launch
                }
            }

            _renameDeckState.value = UiState.Loading

            _renameDeckState.value = try {
                renameDeckUseCase(deckId = deckId, newName = newDeckName)
                toggleRenameModalWindow(false)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(Throwable("Колода с таким названием уже существует."))
            }

            if (_renameDeckState.value is UiState.Success) {
                getDeckById(deckId)
            }
        }
    }

    private val _moveCardsBetweenDecksState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val moveCardsBetweenDecksState: StateFlow<UiState<Unit>> = _moveCardsBetweenDecksState

    fun moveCardsBetweenDecks(
        cardIds: List<Int>,
        sourceDeckId: Int,
        targetDeckId: Int
    ) {
        viewModelScope.launch {
            _moveCardsBetweenDecksState.value = try {
                moveCardsBetweenDeckUseCase(cardIds, sourceDeckId, targetDeckId)
                toggleEditCardsInDeckModalWindow(false)
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

    private val _addCardsToDeck = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val addCardsToDeck: StateFlow<UiState<Unit>> = _addCardsToDeck

    fun addCardsToDeck(
        cardIds: List<Int>,
        targetDeckId: Int
    ) {
        viewModelScope.launch {
            _addCardsToDeck.value = try {
                addCardsToDeckUseCase(cardIds, targetDeckId)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    var renameModalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleRenameModalWindow(switch: Boolean) {
        if (!switch) {
            _renameDeckState.value = UiState.Success(Unit)
        }

        renameModalWindowValue.value = switch
    }

    var editCardsInDeckModalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleEditCardsInDeckModalWindow(switch: Boolean) {
        if (!switch) {
            _moveCardsBetweenDecksState.value = UiState.Success(Unit)
            selectionManager.clearDeckSelection()
        }

        editCardsInDeckModalWindowValue.value = switch
    }

    var deleteDeckModalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleDeleteDeckModalWindow(switch: Boolean) {
        deleteDeckModalWindowValue.value = switch
    }

    var deleteCardsModalWindowValue = MutableStateFlow<Boolean>(false)

    fun toggleDeleteCardsModalWindow(switch: Boolean) {
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

    fun clearCardSelection() {
        selectionManager.clearCardSelection()
        toggleEditMode()
    }
}