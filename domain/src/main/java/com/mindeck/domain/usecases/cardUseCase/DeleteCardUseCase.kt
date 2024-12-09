package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository

class DeleteCardUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(card: Card) {
        return cardRepository.deleteCard(card = card)
    }
}