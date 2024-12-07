package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.repository.CardRepository

class DeleteCardsFromDeckUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(cardsIds: List<Int>, deckId: Int) {
        return cardRepository.deleteCardsFromDeck(cardsIds = cardsIds, deckId = deckId)
    }
}