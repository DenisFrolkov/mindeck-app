package com.mindeck.domain.usecases.deck.command

import com.mindeck.domain.repository.DeckRepository

class DeleteDeckUseCase(
    private val deckRepository: DeckRepository,
) {
    suspend operator fun invoke(deckId: Int) {
        deckRepository.deleteDeck(deckId = deckId)
    }
}
