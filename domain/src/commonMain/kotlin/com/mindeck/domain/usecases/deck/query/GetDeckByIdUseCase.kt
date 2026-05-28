package com.mindeck.domain.usecases.deck.query

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow

class GetDeckByIdUseCase(private val deckRepository: DeckRepository) {
    operator fun invoke(deckId: Int): Flow<Deck?> = deckRepository.getDeckById(deckId = deckId)
}
