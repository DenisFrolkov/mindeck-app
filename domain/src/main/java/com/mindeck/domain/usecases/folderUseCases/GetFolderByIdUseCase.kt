package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.FolderRepository

class GetFolderByIdUseCase(private val folderRepository: FolderRepository) {
    suspend operator fun invoke(folderId: Int): Folder {
        return folderRepository.getFolderById(folderId = folderId)
    }
}