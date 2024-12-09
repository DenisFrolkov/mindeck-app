package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository

class UpdateCardUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(card: Card) {
        return cardRepository.updateCard(card = card)
    }
}