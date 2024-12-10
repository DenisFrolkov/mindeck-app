package com.mindeck.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindeck.domain.models.Folder
import com.mindeck.domain.usecases.folderUseCases.CreateFolderUseCase
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val createFolderUseCase: CreateFolderUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase
) : ViewModel() {
    private val _resultMutable = MutableStateFlow<List<Folder>>(emptyList())
    val result: MutableStateFlow<List<Folder>> = _resultMutable

    // Метод для получения папок
    fun getFolder() {
        viewModelScope.launch {
            getAllFoldersUseCase()
                .collect { folders ->
                    _resultMutable.value = folders // Обновляем состояние
                }
        }
    }

    // Метод для создания папки
    suspend fun create(folder: Folder) {
        createFolderUseCase.invoke(folder)
    }
}