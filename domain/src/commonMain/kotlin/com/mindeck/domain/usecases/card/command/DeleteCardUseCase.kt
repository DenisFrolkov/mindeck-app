package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.CardRepository
class DeleteCardUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(cardId: Int) {
        cardRepository.deleteCard(cardId = cardId)
    }
}
