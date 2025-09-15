package com.mindeck.domain.usecases.deck.command

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import javax.inject.Inject

class CreateDeckUseCase @Inject constructor (private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deck: Deck) {
        deckRepository.insertDeck(deck = deck)
    }
}