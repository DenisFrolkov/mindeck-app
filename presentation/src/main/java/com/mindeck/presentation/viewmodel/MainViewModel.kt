package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.folderUseCases.CreateFolderUseCase
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import com.mindeck.presentation.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val createFolderUseCase: CreateFolderUseCase
) : ViewModel() {

    private val _folderUIState = MutableStateFlow<UiState<List<Folder>>>(UiState.Loading)
    val folderUIState: StateFlow<UiState<List<Folder>>> = _folderUIState

    private val _validation = MutableStateFlow<Boolean?>(null)
    val validation: StateFlow<Boolean?> = _validation.asStateFlow()

    init {
        getAllFolders()
    }

    private fun getAllFolders() {
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

    fun createFolder(folder: Folder) {
        viewModelScope.launch {
            createFolderUseCase.invoke(folder)
        }
    }

    fun validationCreate(folderName: String) {
        _validation.value = folderName == ""
    }
}