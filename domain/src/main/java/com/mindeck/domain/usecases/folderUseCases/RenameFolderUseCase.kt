package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.repository.FolderRepository

class RenameFolderUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folderId: Int, newName: String) {
        return folderRepository.renameFolder(folderId = folderId, newName = newName)
    }
}