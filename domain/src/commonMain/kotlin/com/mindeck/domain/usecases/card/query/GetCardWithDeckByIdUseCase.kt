package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

class GetCardWithDeckByIdUseCase(
    private val cardRepository: CardRepository,
) {
    operator fun invoke(cardId: Int): Flow<CardWithDeck?> = cardRepository.getCardWithDeckById(cardId = cardId)
}
