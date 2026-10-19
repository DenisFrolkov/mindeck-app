package com.mindeck.domain.usecases.deck.query

import app.cash.turbine.test
import com.mindeck.domain.models.DeckWithStats
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.time.FixedTimeProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

class GetDecksWithStatsUseCaseTest {
    private val moscow = TimeZone.of("Europe/Moscow")
    private val currentTime = Instant.parse("2026-03-09T22:00:00Z").toEpochMilliseconds()
    private val todayStart = Instant.parse("2026-03-09T21:00:00Z").toEpochMilliseconds()

    private val repository = mockk<DeckRepository>()
    private val useCase = GetDecksWithStatsUseCase(repository, FixedTimeProvider(currentTime, moscow))

    @Test
    fun `asks the repository for stats with the start of the current local day`() =
        runTest {
            val stats = emptyList<DeckWithStats>()
            every {
                repository.getCardStatsPerDeck(currentTime = currentTime, todayStart = todayStart)
            } returns flowOf(stats)

            useCase(currentTime).test {
                assertEquals(stats, awaitItem())
                awaitComplete()
            }
        }
}
