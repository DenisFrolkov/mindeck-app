package com.mindeck.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.cardUseCase.CreateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksByFolderIdUseCase
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import com.mindeck.presentation.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreationCardViewModel @Inject constructor(
    private val createCardUseCase: CreateCardUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getAllDecksByFolderIdUseCase: GetAllDecksByFolderIdUseCase
) : ViewModel() {

    private val _folderUIState = MutableStateFlow<UiState<List<Folder>>>(UiState.Loading)
    val folderUIState: StateFlow<UiState<List<Folder>>> = _folderUIState

    private val _deckByIdrUIState = MutableStateFlow<UiState<List<Deck>>>(UiState.Loading)
    val deckByIdrUIState: StateFlow<UiState<List<Deck>>> = _deckByIdrUIState

    fun createCard(card: Card) {
        viewModelScope.launch {
            createCardUseCase.invoke(card)
        }
    }

    fun getAllFolders() {
        viewModelScope.launch {
            try {
                _folderUIState.value = UiState.Loading
                getAllFoldersUseCase().collect { folders ->
                    _folderUIState.value = UiState.Success(folders)
                }
            } catch (e: Exception) {
                _folderUIState.value = UiState.Error(e)
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
}