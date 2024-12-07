package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.repository.DeckRepository

class MoveDecksBetweenFoldersUseCase(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deckIds: List<Int>, sourceFolderId: Int, targetFolderId: Int) {
        return deckRepository.moveDecksBetweenFolders(
            deckIds = deckIds,
            sourceFolderId = sourceFolderId,
            targetFolderId = targetFolderId
        )
    }
}