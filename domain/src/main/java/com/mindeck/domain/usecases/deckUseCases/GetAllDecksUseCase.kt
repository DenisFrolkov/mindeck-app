package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow

class GetAllDecksUseCase(private val deckRepository: DeckRepository) {
    operator fun invoke(): Flow<List<Deck>> {
        return deckRepository.getAllDecks()
    }
}