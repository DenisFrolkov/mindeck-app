package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.exception.DomainException
import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.FolderRepository

class DeleteFolderUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folder: Folder) {
        try {
            return folderRepository.deleteFolder(folder = folder)
        } catch (e: DomainException) {
            throw DomainException("Failed to delete folder: ${e.localizedMessage}", e)
        }
    }
}