package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.repository.CardDeckOperations

class DeleteCardsFromDeckUseCase(private val cardDeckOperations: CardDeckOperations) {
    suspend operator fun invoke(cardsIds: List<Int>, deckId: Int) {
        return cardDeckOperations.deleteCardsFromDeck(cardsIds = cardsIds, deckId = deckId)
    }
}