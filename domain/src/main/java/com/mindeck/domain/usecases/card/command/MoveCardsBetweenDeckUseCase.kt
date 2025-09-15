package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.CardDeckOperations
import javax.inject.Inject

class MoveCardsBetweenDeckUseCase @Inject constructor (private val cardDeckOperations: CardDeckOperations) {
    suspend operator fun invoke(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        cardDeckOperations.moveCardsBetweenDeck(
            cardIds = cardIds,
            sourceDeckId = sourceDeckId,
            targetDeckId = targetDeckId
        )
    }
}