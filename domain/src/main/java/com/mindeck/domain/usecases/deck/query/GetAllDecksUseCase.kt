package com.mindeck.domain.usecases.deck.query

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDecksUseCase @Inject constructor (private val deckRepository: DeckRepository) {
    operator fun invoke(): Flow<List<Deck>> {
        return deckRepository.getAllDecks()
    }
}