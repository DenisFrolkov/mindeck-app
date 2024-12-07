package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.repository.DeckRepository

class DeleteDecksFromFolderUseCase(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deckIds: List<Int>, folderId: Int) {
        return deckRepository.deleteDecksFromFolder(deckIds = deckIds, folderId = folderId)
    }
}