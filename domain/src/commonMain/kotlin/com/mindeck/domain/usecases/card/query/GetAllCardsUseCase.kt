package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

class GetAllCardsUseCase(private val cardRepository: CardRepository) {
    operator fun invoke(deckId: Int): Flow<List<Card>> {
        return cardRepository.getAllCardsByDeckId(deckId = deckId)
    }
}
