package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.repository.CardRepository

class AddCardsToDeckUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(cardIds: List<Int>, deckId: Int) {
        return cardRepository.addCardsToDeck(cardIds = cardIds, deckId = deckId)

    }
}