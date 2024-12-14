package com.mindeck.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksByFolderIdUseCase
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
    private val renameFolderUseCase: RenameFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase
) : ViewModel() {
    private val _folderUIState = MutableStateFlow<UiState<Folder>>(UiState.Loading)
    val folderUIState: StateFlow<UiState<Folder>> = _folderUIState

    private val _deckByIdrUIState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val deckByIdrUIState: StateFlow<UiState<List<Deck>>> = _deckByIdrUIState

    init {
        getFolderById(1)
    }

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

    fun renameFolder(folderId: Int, newName: String) {
        viewModelScope.launch {
            renameFolderUseCase.invoke(folderId = folderId, newName = newName)
        }
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            deleteFolderUseCase.invoke(folder)
        }
    }
}