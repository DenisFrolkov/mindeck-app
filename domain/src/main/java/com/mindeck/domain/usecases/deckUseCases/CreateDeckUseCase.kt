package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository

class CreateDeckUseCase(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deck: Deck) {
        return deckRepository.insertDeck(deck = deck)
    }
}