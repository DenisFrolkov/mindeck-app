package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository

class CreateCardUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(card: Card) {
        return cardRepository.insertCard(card = card)
    }
}