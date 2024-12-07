package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.exception.DomainException
import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.FolderRepository

class CreateFolderUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folder: Folder) {
        try {
            folderRepository.createFolder(folder = folder)
        } catch (e: DomainException) {
            throw DomainException("Failed to create folder: ${e.localizedMessage}", e)
        }
    }
}