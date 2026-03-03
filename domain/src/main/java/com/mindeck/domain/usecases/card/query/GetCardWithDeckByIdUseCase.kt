package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.repository.CardRepository
import javax.inject.Inject

class GetCardWithDeckByIdUseCase @Inject constructor(
    private val cardRepository: CardRepository,
) {
    suspend operator fun invoke(cardId: Int): CardWithDeck {
        return cardRepository.getCardWithDeckById(cardId = cardId)
    }
}
