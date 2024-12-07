package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.repository.DeckRepository

class RenameDeckUseCase(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deckId: Int, newName: String) {
        return deckRepository.renameDeck(deckId = deckId, newName = newName)
    }
}