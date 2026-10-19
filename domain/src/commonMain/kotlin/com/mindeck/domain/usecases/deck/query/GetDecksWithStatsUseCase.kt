package com.mindeck.domain.usecases.deck.query

import com.mindeck.domain.models.DeckWithStats
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.time.TimeProvider
import com.mindeck.domain.time.startOfDayMillis
import kotlinx.coroutines.flow.Flow

class GetDecksWithStatsUseCase(
    private val deckRepository: DeckRepository,
    private val clock: TimeProvider,
) {
    operator fun invoke(currentTime: Long): Flow<List<DeckWithStats>> =
        deckRepository.getCardStatsPerDeck(
            currentTime = currentTime,
            todayStart = startOfDayMillis(currentTime, clock.timeZone()),
        )
}
