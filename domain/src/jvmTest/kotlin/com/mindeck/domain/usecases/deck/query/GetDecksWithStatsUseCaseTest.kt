package com.mindeck.domain.usecases.deck.query

import app.cash.turbine.test
import com.mindeck.domain.models.DeckWithStats
import com.mindeck.domain.repository.DeckRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDecksWithStatsUseCaseTest {
    private val dayMs = 86_400_000L
    private val currentTime = dayMs * 10 + dayMs / 2
    private val todayStart = dayMs * 10

    private val repository = mockk<DeckRepository>()
    private val useCase = GetDecksWithStatsUseCase(repository)

    @Test
    fun `asks the repository for stats with the start of the current day`() =
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
