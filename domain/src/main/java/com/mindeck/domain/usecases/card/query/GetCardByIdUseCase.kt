package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository
import javax.inject.Inject

class GetCardByIdUseCase @Inject constructor (private val cardRepository: CardRepository) {
    suspend operator fun invoke(cardId: Int): Card {
        return cardRepository.getCardById(cardId = cardId)
    }
}