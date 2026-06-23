package com.mindeck.domain.usecases.deck.query

import com.mindeck.domain.models.DeckWithStats
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow

class GetDecksWithStatsUseCase(
    private val deckRepository: DeckRepository,
) {
    operator fun invoke(currentTime: Long): Flow<List<DeckWithStats>> = deckRepository.getCardStatsPerDeck(currentTime = currentTime)
}
