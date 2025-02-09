package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.deckUseCases.AddDecksToFolderUseCase
import com.mindeck.domain.usecases.deckUseCases.CreateDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDecksFromFolderUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksByFolderIdUseCase
import com.mindeck.domain.usecases.deckUseCases.MoveDecksBetweenFoldersUseCase
import com.mindeck.domain.usecases.folderUseCases.DeleteFolderUseCase
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import com.mindeck.domain.usecases.folderUseCases.GetFolderByIdUseCase
import com.mindeck.domain.usecases.folderUseCases.RenameFolderUseCase
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.UiState.Loading.mapToUiState
import com.mindeck.presentation.viewmodel.managers.EditModeManager
import com.mindeck.presentation.viewmodel.managers.SelectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getAllDecksByFolderIdUseCase: GetAllDecksByFolderIdUseCase,
    private val getFolderByIdUseCase: GetFolderByIdUseCase,
    private val moveDecksBetweenFoldersUseCase: MoveDecksBetweenFoldersUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
    private val renameFolderUseCase: RenameFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val deleteDecksFromFolderUseCase: DeleteDecksFromFolderUseCase,
    private val addDecksToFolderUseCase: AddDecksToFolderUseCase,
    private val editModeManager: EditModeManager,
    private val selectionManager: SelectionManager,
) : ViewModel() {

    private val _decksByFolderId = MutableStateFlow<List<Deck>>(emptyList())
    private val _decksByFolderIdState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val decksByFolderIdState: StateFlow<UiState<List<Deck>>> = _decksByFolderIdState

    fun loadDecksByFolder(folderId: Int) {
        viewModelScope.launch {
            _decksByFolderId.value = emptyList()
            getAllDecksByFolderIdUseCase(folderId)
                .map<List<Deck>, UiState<List<Deck>>> { UiState.Success(it) }
                .catch { emit(UiState.Error(it)) }
                .collect { state ->
                    _decksByFolderIdState.value = state
                }
        }
    }

    private val _folderByFolderIdUIState = MutableStateFlow<UiState<Folder>>(UiState.Loading)
    val folderByFolderIdUIState: StateFlow<UiState<Folder>> = _folderByFolderIdUIState

    fun loadFolderByFolderId(folderId: Int) {
        viewModelScope.launch {
            _folderByFolderIdUIState.value = try {
                UiState.Success(getFolderByIdUseCase(folderId))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _createDeckState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val createDeckState: StateFlow<UiState<Unit>> = _createDeckState

    fun createDeck(
        deckName: String,
        folderId: Int,
    ) {
        viewModelScope.launch {
            _createDeckState.value = try {
                createDeckUseCase(Deck(deckName = deckName, folderId = folderId))
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    val foldersState: StateFlow<UiState<List<Folder>>> = getAllFoldersUseCase()
        .map<List<Folder>, UiState<List<Folder>>> { UiState.Success(it) }
        .catch { emit(UiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    private val _renameDeckState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val renameDeckState: StateFlow<UiState<Unit>> = _renameDeckState

    fun renameFolder(folderId: Int, newFolderName: String) {
        viewModelScope.launch {
            _renameDeckState.value = try {
                renameFolderUseCase(folderId = folderId, newName = newFolderName)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _deleteFolderState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteFolderState: StateFlow<UiState<Unit>> = _deleteFolderState

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            _deleteFolderState.value = try {
                deleteFolderUseCase(folder)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _moveDecksBetweenFoldersState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val moveDecksBetweenFoldersState: StateFlow<UiState<Unit>> = _moveDecksBetweenFoldersState


    fun moveDecksBetweenFolders(
        deckIds: List<Int>,
        sourceFolderId: Int,
        targetFolderId: Int
    ) {
        viewModelScope.launch {
            _moveDecksBetweenFoldersState.value = try {
                moveDecksBetweenFoldersUseCase(deckIds, sourceFolderId, targetFolderId)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    private val _addDecksToFolder = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val addDecksToFolder: StateFlow<UiState<Unit>> = _addDecksToFolder

    fun addDecksToFolder(
        deckIds: List<Int>,
        targetFolderId: Int
    ) {
        viewModelScope.launch {
            _addDecksToFolder.value = try {
                addDecksToFolderUseCase(deckIds, targetFolderId)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    val isEditModeEnabled: StateFlow<Boolean> = editModeManager.isEditModeEnabled

    fun toggleEditMode() {
        if (_decksByFolderId.value.isNotEmpty()) {
            editModeManager.toggleEditMode()
        }
    }

    val selectedDeckIdSet: StateFlow<Set<Int>> = selectionManager.selectedItemIds

    fun toggleDeckSelection(deckId: Int) {
        selectionManager.toggleDeckSelection(deckId)
    }

    fun clearSelectedDeck() {
        selectionManager.clearSelectedDeck()
        toggleEditMode()
    }
}