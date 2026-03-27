package com.mindeck.domain.usecases.card.query

import app.cash.turbine.test
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.ClockRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCardsRepetitionUseCaseTest {

    private val DAY_MS = 86_400_000L
    private val now = DAY_MS * 10 + DAY_MS / 2
    private val todayStart = DAY_MS * 10

    private val repository = mockk<CardRepetitionRepository>()
    private val clock = mockk<ClockRepository>()
    private val useCase = GetCardsRepetitionUseCase(repository, clock)

    private fun card(
        cardState: CardState,
        nextReviewDate: Long? = null,
        firstReviewDate: Long? = null,
    ) = Card(
        cardName = "Test",
        cardQuestion = "Q?",
        cardAnswer = "A",
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
        cardState = cardState,
        nextReviewDate = nextReviewDate,
        firstReviewDate = firstReviewDate,
    )

    private fun setupClock() {
        every { clock.now() } returns now
    }

    private fun setupRepository(cards: List<Card>) {
        every {
            repository.getCardsRepetition(currentTime = now, todayStart = todayStart)
        } returns flowOf(cards)
    }

    @Test
    fun `LEARNING card with null nextReviewDate is included in session`() = runTest {
        val learningCard = card(cardState = CardState.LEARNING, nextReviewDate = null)
        setupClock()
        setupRepository(listOf(learningCard))

        useCase().test {
            val result = awaitItem()
            assertTrue(result.contains(learningCard))
            awaitComplete()
        }
    }

    @Test
    fun `LEARNING card with future nextReviewDate is NOT included in session`() = runTest {
        val learningCard = card(cardState = CardState.LEARNING, nextReviewDate = now + 1L)
        setupClock()
        setupRepository(listOf(learningCard))

        useCase().test {
            val result = awaitItem()
            assertFalse(result.contains(learningCard))
            awaitComplete()
        }
    }

    @Test
    fun `NEW cards are limited by DAILY_NEW_LIMIT`() = runTest {
        val newCards = List(21) { card(cardState = CardState.NEW) }
        setupClock()
        setupRepository(newCards)

        useCase().test {
            val result = awaitItem()
            assertEquals(20, result.size)
            awaitComplete()
        }
    }

    @Test
    fun `NEW cards count is reduced by cards already shown today`() = runTest {
        val newCards = List(20) { card(cardState = CardState.NEW, firstReviewDate = null) }
        val reviewTodayCards =
            List(5) { card(cardState = CardState.LEARNING, firstReviewDate = now) }
        setupClock()
        setupRepository(newCards + reviewTodayCards)

        useCase().test {
            val result = awaitItem()
            assertEquals(15, result.count { it.cardState == CardState.NEW })
            awaitComplete()
        }
    }

    @Test
    fun `total session size includes LEARNING and limited NEW cards`() = runTest {
        val newCards = List(20) { card(cardState = CardState.NEW, firstReviewDate = null) }
        val reviewTodayCards =
            List(5) { card(cardState = CardState.LEARNING, firstReviewDate = now) }
        setupClock()
        setupRepository(newCards + reviewTodayCards)

        useCase().test {
            val result = awaitItem()
            assertEquals(20, result.size)
            awaitComplete()
        }
    }

    @Test
    fun `session order is LEARNING then REVIEW then NEW`() = runTest {
        val learningCards = List(2) { card(cardState = CardState.LEARNING, nextReviewDate = null) }
        val reviewCards = List(2) { card(cardState = CardState.REVIEW, nextReviewDate = now - 1L) }
        val newCards = List(2) { card(cardState = CardState.NEW) }
        setupClock()
        setupRepository(newCards + reviewCards + learningCards)

        useCase().test {
            val result = awaitItem()
            assertEquals(CardState.LEARNING, result[0].cardState)
            assertEquals(CardState.REVIEW, result[2].cardState)
            assertEquals(CardState.NEW, result[4].cardState)
            awaitComplete()
        }
    }

    @Test
    fun `REVIEW card with expired nextReviewDate is included in session`() = runTest {
        val reviewCard = card(cardState = CardState.REVIEW, nextReviewDate = now - 1L)
        setupClock()
        setupRepository(listOf(reviewCard))

        useCase().test {
            val result = awaitItem()
            assertTrue(result.contains(reviewCard))
            awaitComplete()
        }
    }

    @Test
    fun `REVIEW card with null nextReviewDate is NOT included in session`() = runTest {
        val reviewCard = card(cardState = CardState.REVIEW, nextReviewDate = null)
        setupClock()
        setupRepository(listOf(reviewCard))

        useCase().test {
            val result = awaitItem()
            assertFalse(result.contains(reviewCard))
            awaitComplete()
        }
    }

    @Test
    fun `LAPSE card with null nextReviewDate is included in session`() = runTest {
        val lapseCard = card(cardState = CardState.LAPSE, nextReviewDate = null)
        setupClock()
        setupRepository(listOf(lapseCard))

        useCase().test {
            val result = awaitItem()
            assertTrue(result.contains(lapseCard))
            awaitComplete()
        }
    }

    @Test
    fun `NEW cards are not shown when daily limit is exhausted`() = runTest {
        val shownTodayCards =
            List(20) { card(cardState = CardState.LEARNING, firstReviewDate = now) }
        val newCards = List(5) { card(cardState = CardState.NEW) }
        setupClock()
        setupRepository(shownTodayCards + newCards)

        useCase().test {
            val result = awaitItem()
            assertEquals(0, result.count { it.cardState == CardState.NEW })
            awaitComplete()
        }
    }

    @Test
    fun `LAPSE card with future nextReviewDate is NOT included in session`() = runTest {
        val lapseCard = card(cardState = CardState.LAPSE, nextReviewDate = now + 1L)
        setupClock()
        setupRepository(listOf(lapseCard))

        useCase().test {
            val result = awaitItem()
            assertFalse(result.contains(lapseCard))
            awaitComplete()
        }
    }

    @Test
    fun `empty card list returns empty session`() = runTest {
        setupClock()
        setupRepository(emptyList())

        useCase().test {
            val result = awaitItem()
            assertTrue(result.isEmpty())
            awaitComplete()
        }
    }
}
