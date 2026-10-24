package com.mindeck.domain.usecases.deck.query

import com.mindeck.domain.models.DeckWithStats
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.time.TimeProvider
import com.mindeck.domain.time.startOfDayMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetDecksWithStatsUseCase(
    private val deckRepository: DeckRepository,
    private val clock: TimeProvider,
) {
    operator fun invoke(): Flow<List<DeckWithStats>> =
        flow {
            val now = clock.now()
            emitAll(
                deckRepository.getCardStatsPerDeck(
                    currentTime = now,
                    todayStart = startOfDayMillis(now, clock.timeZone()),
                ),
            )
        }
}
