package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.exception.DomainException
import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow

class GetAllFoldersUseCase(private val folderRepository: FolderRepository) {
    operator fun invoke(): Flow<List<Folder>> {
        try {
            return folderRepository.getAllFolders()
        } catch (e: DomainException) {
            throw DomainException("Failed to create folder: ${e.localizedMessage}", e)
        }
    }
}