package com.mindeck.domain.usecases.deck.command

import com.mindeck.domain.repository.DeckRepository

class RenameDeckUseCase(
    private val deckRepository: DeckRepository,
) {
    suspend operator fun invoke(
        deckId: Int,
        newName: String,
    ) {
        deckRepository.renameDeck(deckId = deckId, newName = newName)
    }
}
