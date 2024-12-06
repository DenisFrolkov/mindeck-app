package com.mindeck.domain.usecases.folderUseCases

import com.mindeck.domain.repository.FolderRepository

class GetAllFoldersUseCase(private val folderRepository: FolderRepository) {
    operator fun invoke() {
        folderRepository.getAllFolders()
    }
}