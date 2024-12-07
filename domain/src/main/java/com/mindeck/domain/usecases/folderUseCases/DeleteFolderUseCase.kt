package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.FolderRepository

class DeleteFolderUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folder: Folder) {
        return folderRepository.deleteFolder(folder = folder)
    }
}