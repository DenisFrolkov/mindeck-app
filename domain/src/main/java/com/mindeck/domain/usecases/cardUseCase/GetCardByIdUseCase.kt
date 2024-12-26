package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository

class GetCardByIdUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(cardId: Int): Card {
        return cardRepository.getCardById(cardId = cardId)
    }
}