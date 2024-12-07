package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.repository.CardRepository

class MoveCardsBetweenDeckUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        return cardRepository.moveCardsBetweenDeck(
            cardIds = cardIds,
            sourceDeckId = sourceDeckId,
            targetDeckId = targetDeckId
        )
    }
}