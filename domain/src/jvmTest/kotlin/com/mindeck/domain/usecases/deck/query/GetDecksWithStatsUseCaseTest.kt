package com.mindeck.domain.usecases.deck.query

import app.cash.turbine.test
import com.mindeck.domain.models.DeckWithStats
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.time.FixedTimeProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
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
    private val clock = FixedTimeProvider(currentTime, moscow)
    private val useCase = GetDecksWithStatsUseCase(repository, clock)

    @Test
    fun `asks the repository for stats with the start of the current local day`() =
        runTest {
            val stats = emptyList<DeckWithStats>()
            every {
                repository.getCardStatsPerDeck(currentTime = currentTime, todayStart = todayStart)
            } returns flowOf(stats)

            useCase().test {
                assertEquals(stats, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `reads the clock on every collection`() =
        runTest {
            val beforeMidnight = Instant.parse("2026-03-09T20:30:00Z").toEpochMilliseconds()
            val previousDayStart = Instant.parse("2026-03-08T21:00:00Z").toEpochMilliseconds()
            every { repository.getCardStatsPerDeck(any(), any()) } returns flowOf(emptyList())
            val stats = useCase()

            clock.now = beforeMidnight
            stats.first()
            clock.now = currentTime
            stats.first()

            verify(exactly = 1) {
                repository.getCardStatsPerDeck(currentTime = beforeMidnight, todayStart = previousDayStart)
            }
            verify(exactly = 1) {
                repository.getCardStatsPerDeck(currentTime = currentTime, todayStart = todayStart)
            }
        }
}
