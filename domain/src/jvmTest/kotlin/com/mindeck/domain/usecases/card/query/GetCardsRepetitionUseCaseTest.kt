package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.time.FixedTimeProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCardsRepetitionUseCaseTest {
    private val dayMs = 86_400_000L
    private val now = dayMs * 10 + dayMs / 2
    private val todayStart = dayMs * 10

    private val repository = mockk<CardRepetitionRepository>()
    private val clock = FixedTimeProvider(now)
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

    private fun setupRepository(cards: List<Card>) {
        coEvery {
            repository.getCardsRepetition(currentTime = now, todayStart = todayStart)
        } returns cards
        coEvery { repository.getLearnAheadCard(any()) } returns null
    }

    @Test
    fun `LEARNING card with null nextReviewDate is included in session`() =
        runTest {
            val learningCard = card(cardState = CardState.LEARNING, nextReviewDate = null)
            setupRepository(listOf(learningCard))

            val result = useCase()
            assertTrue(result.contains(learningCard))
        }

    @Test
    fun `LEARNING card with future nextReviewDate is NOT included in session`() =
        runTest {
            val learningCard = card(cardState = CardState.LEARNING, nextReviewDate = now + 1L)
            setupRepository(listOf(learningCard))

            val result = useCase()
            assertFalse(result.contains(learningCard))
        }

    @Test
    fun `NEW cards are limited by DAILY_NEW_LIMIT`() =
        runTest {
            val newCards = List(21) { card(cardState = CardState.NEW) }
            setupRepository(newCards)

            val result = useCase()
            assertEquals(20, result.size)
        }

    @Test
    fun `NEW cards count is reduced by cards already shown today`() =
        runTest {
            val newCards = List(20) { card(cardState = CardState.NEW, firstReviewDate = null) }
            val reviewTodayCards =
                List(5) { card(cardState = CardState.LEARNING, firstReviewDate = now) }
            setupRepository(newCards + reviewTodayCards)

            val result = useCase()
            assertEquals(15, result.count { it.cardState == CardState.NEW })
        }

    @Test
    fun `total session size includes LEARNING and limited NEW cards`() =
        runTest {
            val newCards = List(20) { card(cardState = CardState.NEW, firstReviewDate = null) }
            val reviewTodayCards =
                List(5) { card(cardState = CardState.LEARNING, firstReviewDate = now) }
            setupRepository(newCards + reviewTodayCards)

            val result = useCase()
            assertEquals(20, result.size)
        }

    @Test
    fun `session order is LEARNING then REVIEW then NEW`() =
        runTest {
            val learningCards = List(2) { card(cardState = CardState.LEARNING, nextReviewDate = null) }
            val reviewCards = List(2) { card(cardState = CardState.REVIEW, nextReviewDate = now - 1L) }
            val newCards = List(2) { card(cardState = CardState.NEW) }
            setupRepository(newCards + reviewCards + learningCards)

            val result = useCase()
            assertEquals(CardState.LEARNING, result[0].cardState)
            assertEquals(CardState.REVIEW, result[2].cardState)
            assertEquals(CardState.NEW, result[4].cardState)
        }

    @Test
    fun `REVIEW card with expired nextReviewDate is included in session`() =
        runTest {
            val reviewCard = card(cardState = CardState.REVIEW, nextReviewDate = now - 1L)
            setupRepository(listOf(reviewCard))

            val result = useCase()
            assertTrue(result.contains(reviewCard))
        }

    @Test
    fun `REVIEW card with null nextReviewDate is NOT included in session`() =
        runTest {
            val reviewCard = card(cardState = CardState.REVIEW, nextReviewDate = null)
            setupRepository(listOf(reviewCard))

            val result = useCase()
            assertFalse(result.contains(reviewCard))
        }

    @Test
    fun `LAPSE card with null nextReviewDate is included in session`() =
        runTest {
            val lapseCard = card(cardState = CardState.LAPSE, nextReviewDate = null)
            setupRepository(listOf(lapseCard))

            val result = useCase()
            assertTrue(result.contains(lapseCard))
        }

    @Test
    fun `NEW cards are not shown when daily limit is exhausted`() =
        runTest {
            val shownTodayCards =
                List(20) { card(cardState = CardState.LEARNING, firstReviewDate = now) }
            val newCards = List(5) { card(cardState = CardState.NEW) }
            setupRepository(shownTodayCards + newCards)

            val result = useCase()
            assertEquals(0, result.count { it.cardState == CardState.NEW })
        }

    @Test
    fun `LAPSE card with future nextReviewDate is NOT included in session`() =
        runTest {
            val lapseCard = card(cardState = CardState.LAPSE, nextReviewDate = now + 1L)
            setupRepository(listOf(lapseCard))

            val result = useCase()
            assertFalse(result.contains(lapseCard))
        }

    @Test
    fun `empty card list returns empty session`() =
        runTest {
            setupRepository(emptyList())

            val result = useCase()
            assertTrue(result.isEmpty())
        }

    @Test
    fun `card that becomes due after time passes is included on the next call`() =
        runTest {
            val learningCard = card(cardState = CardState.LEARNING, nextReviewDate = now + 60_000L)
            val later = now + 60_000L
            coEvery {
                repository.getCardsRepetition(currentTime = now, todayStart = todayStart)
            } returns emptyList()
            coEvery {
                repository.getCardsRepetition(currentTime = later, todayStart = todayStart)
            } returns listOf(learningCard)
            coEvery { repository.getLearnAheadCard(any()) } returns null

            assertTrue(useCase().isEmpty())
            clock.now = later

            assertEquals(listOf(learningCard), useCase())
        }

    @Test
    fun `empty queue falls back to learn-ahead card within the window`() =
        runTest {
            val learningCard =
                card(
                    cardState = CardState.LEARNING,
                    nextReviewDate = now + GetCardsRepetitionUseCase.LEARN_AHEAD_MILLIS,
                )
            setupRepository(emptyList())
            coEvery {
                repository.getLearnAheadCard(now + GetCardsRepetitionUseCase.LEARN_AHEAD_MILLIS)
            } returns learningCard

            assertEquals(listOf(learningCard), useCase())
        }

    @Test
    fun `empty queue and no learn-ahead card returns empty session`() =
        runTest {
            setupRepository(emptyList())

            assertTrue(useCase().isEmpty())
        }

    @Test
    fun `learn-ahead is not queried when the queue is not empty`() =
        runTest {
            setupRepository(listOf(card(cardState = CardState.LEARNING, nextReviewDate = null)))

            useCase()

            coVerify(exactly = 0) { repository.getLearnAheadCard(any()) }
        }
}
