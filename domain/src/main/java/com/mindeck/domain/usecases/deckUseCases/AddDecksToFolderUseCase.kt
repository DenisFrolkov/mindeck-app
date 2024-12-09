package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.repository.DeckRepository

class AddDecksToFolderUseCase(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deckIds: List<Int>, folderId: Int) {
        return deckRepository.addDecksToFolder(deckIds = deckIds, folderId = folderId)

    }
}