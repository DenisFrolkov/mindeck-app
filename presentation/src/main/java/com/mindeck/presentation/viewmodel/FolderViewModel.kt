package com.mindeck.presentation.viewmodel

import android.util.Log
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
import com.mindeck.domain.usecases.folderUseCases.GetFolderByIdUseCase
import com.mindeck.domain.usecases.folderUseCases.RenameFolderUseCase
import com.mindeck.presentation.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val getFolderByIdUseCase: GetFolderByIdUseCase,
    private val getAllDecksByFolderIdUseCase: GetAllDecksByFolderIdUseCase,
    private val createDeckUseCase: CreateDeckUseCase,
    private val renameFolderUseCase: RenameFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val deleteDecksFromFolderUseCase: DeleteDecksFromFolderUseCase,
    private val addDecksToFolderUseCase: AddDecksToFolderUseCase,
    private val moveDecksBetweenFoldersUseCase: MoveDecksBetweenFoldersUseCase
) : ViewModel() {
    private val _folderUIState = MutableStateFlow<UiState<Folder>>(UiState.Loading)
    val folderUIState: StateFlow<UiState<Folder>> = _folderUIState

    private val _deckByIdrUIState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val deckByIdrUIState: StateFlow<UiState<List<Deck>>> = _deckByIdrUIState

    fun getFolderById(folderId: Int) {
        viewModelScope.launch {
            try {
                val folder = getFolderByIdUseCase(folderId = folderId)
                _folderUIState.value = UiState.Success(folder)
            } catch (e: Exception) {
                _folderUIState.value = UiState.Error(e)
                Log.e("FolderViewModel", "Error fetching folder: ${e.message}")
            }
        }
    }

    fun getAllDecksByFolderId(folderId: Int) {
        viewModelScope.launch {
            try {
                getAllDecksByFolderIdUseCase(folderId = folderId).collect { decks ->
                    _deckByIdrUIState.value = UiState.Success(decks)
                }
            } catch (e: Exception) {
                _deckByIdrUIState.value = UiState.Error(e)
                Log.e("FolderViewModel", "Error fetching deck: ${e.message}")
            }
        }
    }

    fun createDeck(deck: Deck) {
        viewModelScope.launch {
            createDeckUseCase.invoke(deck = deck)
        }
    }

    fun renameFolder(folderId: Int, newFolderName: String) {
        viewModelScope.launch {
            renameFolderUseCase.invoke(folderId = folderId, newName = newFolderName)
        }
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            deleteFolderUseCase.invoke(folder)
        }
    }

    fun addDecksToFolder(deckIds: List<Int>, folderId: Int) {
        viewModelScope.launch {
            addDecksToFolderUseCase.invoke(deckIds = deckIds, folderId = folderId)
        }
    }

    fun deleteDecksFromFolder(deckIds: List<Int>, folderId: Int) {
        viewModelScope.launch {
            deleteDecksFromFolderUseCase.invoke(deckIds = deckIds, folderId = folderId)
        }
    }

    fun moveDecksBetweenFoldersUseCase(
        deckIds: List<Int>,
        sourceFolderId: Int,
        targetFolderId: Int
    ) {
        viewModelScope.launch {
            moveDecksBetweenFoldersUseCase.invoke(deckIds, sourceFolderId, targetFolderId)
        }
    }
}