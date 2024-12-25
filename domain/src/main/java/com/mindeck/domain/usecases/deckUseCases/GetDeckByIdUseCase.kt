package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository

class GetDeckByIdUseCase(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deckId: Int): Deck {
        return deckRepository.getDeckById(deckId = deckId)
    }
}