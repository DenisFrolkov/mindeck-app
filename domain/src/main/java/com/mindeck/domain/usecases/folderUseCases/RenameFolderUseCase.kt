package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.exception.DomainException
import com.mindeck.domain.repository.FolderRepository

class RenameFolderUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folderId: Int, newName: String) {
        try {
            return folderRepository.renameFolder(folderId = folderId, newName = newName)
        } catch (e: DomainException) {
            throw DomainException("Failed to create folder: ${e.localizedMessage}", e)
        }
    }
}