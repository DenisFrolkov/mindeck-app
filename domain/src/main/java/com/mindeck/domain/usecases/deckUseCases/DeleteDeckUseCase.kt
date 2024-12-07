package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository

class DeleteDeckUseCase(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deck: Deck) {
        return deckRepository.deleteDeck(deck = deck)
    }
}