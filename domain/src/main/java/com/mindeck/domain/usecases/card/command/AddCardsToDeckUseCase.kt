package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.CardDeckOperations
import javax.inject.Inject

class AddCardsToDeckUseCase @Inject constructor(private val cardDeckOperations: CardDeckOperations) {
    suspend operator fun invoke(cardIds: List<Int>, deckId: Int) {
        cardDeckOperations.addCardsToDeck(cardIds = cardIds, deckId = deckId)
    }
}
