package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.repository.CardDeckOperations

class AddCardsToDeckUseCase(private val cardDeckOperations: CardDeckOperations) {
    suspend operator fun invoke(cardIds: List<Int>, deckId: Int) {
        return cardDeckOperations.addCardsToDeck(cardIds = cardIds, deckId = deckId)
    }
}