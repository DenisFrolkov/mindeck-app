package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow

class GetAllFoldersUseCase(private val folderRepository: FolderRepository) {
    operator fun invoke(): Flow<List<Folder>> {
        return folderRepository.getAllFolders()
    }
}