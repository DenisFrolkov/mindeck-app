package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.CardDeckOperations
import javax.inject.Inject

class DeleteCardsFromDeckUseCase @Inject constructor (private val cardDeckOperations: CardDeckOperations) {
    suspend operator fun invoke(cardsIds: List<Int>, deckId: Int) {
        cardDeckOperations.deleteCardsFromDeck(cardsIds = cardsIds, deckId = deckId)
    }
}