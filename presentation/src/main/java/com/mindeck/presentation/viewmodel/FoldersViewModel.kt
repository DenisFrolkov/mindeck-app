package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.folderUseCases.CreateFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import com.mindeck.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val createFolderUseCase: CreateFolderUseCase
) : ViewModel() {

    private val _folderUIState = MutableStateFlow<UiState<List<Folder>>>(UiState.Loading)
    val folderUIState: StateFlow<UiState<List<Folder>>> = _folderUIState

    init {
        getAllFolders()
    }

    private fun getAllFolders() {
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

    fun createFolder(folder: Folder) {
        viewModelScope.launch {
            createFolderUseCase.invoke(folder)
        }
    }
}