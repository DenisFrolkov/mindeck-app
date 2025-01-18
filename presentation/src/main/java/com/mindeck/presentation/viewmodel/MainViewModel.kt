package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.folderUseCases.CreateFolderUseCase
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import com.mindeck.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val createFolderUseCase: CreateFolderUseCase
) : ViewModel() {

    private val _folderUIState = MutableStateFlow<UiState<List<Folder>>>(UiState.Loading)
    val folderUIState: StateFlow<UiState<List<Folder>>> = _folderUIState

    fun getAllFolders() {
        viewModelScope.launch {
            try {
                getAllFoldersUseCase().collect { folders ->
                    _folderUIState.value = UiState.Success(folders)
                }
            } catch (e: Exception) {
                _folderUIState.value = UiState.Error(e)
            }
        }
    }

    fun createFolder(folderName: String) {
        viewModelScope.launch {
            createFolderUseCase.invoke(Folder(folderName = folderName))
        }
    }
}