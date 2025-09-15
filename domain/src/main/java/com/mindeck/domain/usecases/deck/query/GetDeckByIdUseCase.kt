package com.mindeck.domain.usecases.deck.query

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import javax.inject.Inject

class GetDeckByIdUseCase @Inject constructor (private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deckId: Int): Deck {
        return deckRepository.getDeckById(deckId = deckId)
    }
}