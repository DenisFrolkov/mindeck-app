package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

class GetAllCardsByDeckIdUseCase(private val cardRepository: CardRepository) {
    operator fun invoke(deckId: Int): Flow<List<Card>> {
        return cardRepository.getAllCardsByDeckId(deckId = deckId)
    }
}