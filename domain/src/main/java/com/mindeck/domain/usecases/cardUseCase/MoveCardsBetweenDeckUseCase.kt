package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.repository.CardDeckOperations

class MoveCardsBetweenDeckUseCase(private val cardDeckOperations: CardDeckOperations) {
    suspend operator fun invoke(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        return cardDeckOperations.moveCardsBetweenDeck(
            cardIds = cardIds,
            sourceDeckId = sourceDeckId,
            targetDeckId = targetDeckId
        )
    }
}