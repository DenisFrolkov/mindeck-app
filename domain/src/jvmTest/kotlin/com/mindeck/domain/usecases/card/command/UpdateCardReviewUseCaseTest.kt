package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.ReviewButton
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.service.ClockRepository
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class UpdateCardReviewUseCaseTest {

    private val DAY_MS = 86_400_000L
    private val now = DAY_MS * 10 + DAY_MS / 2
    private val repository = mockk<CardRepetitionRepository>()
    private val clock = ClockRepository { now }
    private val useCase = UpdateCardReviewUseCase(repository, clock)

    private fun newCard() = Card(
        cardName = "Test",
        cardQuestion = "Q?",
        cardAnswer = "A",
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
        cardState = CardState.NEW,
        easeFactor = 2.5f,
    )

    private fun setupRepository() {
        coEvery { repository.updateReview(any()) } just runs
    }

    @Test
    fun `NEW + AGAIN transitions card to LEARNING with reduced easeFactor`() = runTest {
        val card = newCard()
        setupRepository()

        val result = useCase(card, ReviewButton.AGAIN)

        assertEquals(CardState.LEARNING, result.cardState)
        assertEquals(1.96f, result.easeFactor, 0.001f)
        assertEquals(0, result.learningStep)
        assertEquals(1, result.repetitionCount)
    }

    @Test
    fun `NEW + HARD transitions card to LEARNING with reduced easeFactor`() = runTest {
        val card = newCard()
        setupRepository()

        val result = useCase(card, ReviewButton.HARD)

        assertEquals(CardState.LEARNING, result.cardState)
        assertEquals(2.36f, result.easeFactor, 0.001f)
        assertEquals(0, result.learningStep)
        assertEquals(1, result.repetitionCount)
    }

    @Test
    fun `NEW + GOOD advances learning step to 1`() = runTest {
        val card = newCard()
        setupRepository()

        val result = useCase(card, ReviewButton.GOOD)

        assertEquals(CardState.LEARNING, result.cardState)
        assertEquals(2.5f, result.easeFactor, 0.001f)
        assertEquals(1, result.learningStep)
        assertEquals(1, result.repetitionCount)
    }

    @Test
    fun `NEW + GOOD on last learning step graduates card to REVIEW`() = runTest {
        val card = newCard().copy(learningStep = 1)
        setupRepository()

        val result = useCase(card, ReviewButton.GOOD)

        assertEquals(CardState.REVIEW, result.cardState)
        assertEquals(1f, result.interval, 0.001f)
        assertEquals(0, result.learningStep)
    }

    @Test
    fun `NEW + EASY immediately graduates card to REVIEW with interval 4`() = runTest {
        val card = newCard()
        setupRepository()

        val result = useCase(card, ReviewButton.EASY)

        assertEquals(CardState.REVIEW, result.cardState)
        assertEquals(4f, result.interval, 0.001f)
        assertEquals(2.60f, result.easeFactor, 0.001f)
        assertEquals(0, result.learningStep)
    }

    @Test
    fun `REVIEW + HARD multiplies interval by 120 percent and reduces easeFactor`() = runTest {
        val card = newCard().copy(cardState = CardState.REVIEW, interval = 5f)
        setupRepository()

        val result = useCase(card, ReviewButton.HARD)

        assertEquals(CardState.REVIEW, result.cardState)
        assertEquals(2.36f, result.easeFactor, 0.001f)
        assertEquals(6f, result.interval, 0.001f)
        assertEquals(1, result.repetitionCount)
        assertEquals(DAY_MS * 16, result.nextReviewDate)
    }

    @Test
    fun `REVIEW + GOOD multiplies interval by easeFactor`() = runTest {
        val card = newCard().copy(cardState = CardState.REVIEW, interval = 5f)
        setupRepository()

        val result = useCase(card, ReviewButton.GOOD)

        assertEquals(CardState.REVIEW, result.cardState)
        assertEquals(12.5f, result.interval, 0.001f)
        assertEquals(2.5f, result.easeFactor, 0.001f)
        assertEquals(DAY_MS * 23, result.nextReviewDate)
        assertEquals(1, result.repetitionCount)
    }

    @Test
    fun `REVIEW + EASY multiplies interval by easeFactor and 1_3x and increases easeFactor`() =
        runTest {
            val card =
                newCard().copy(cardState = CardState.REVIEW, interval = 5f, easeFactor = 2.5f)
            setupRepository()

            val result = useCase(card, ReviewButton.EASY)

            assertEquals(CardState.REVIEW, result.cardState)
            assertEquals(2.6f, result.easeFactor, 0.001f)
            assertEquals(16.25f, result.interval, 0.001f)
            assertEquals(1, result.repetitionCount)
            assertEquals(DAY_MS * 27, result.nextReviewDate)
        }

    @Test
    fun `easeFactor does not drop below MIN_EASE_FACTOR on AGAIN`() = runTest {
        val card = newCard().copy(cardState = CardState.REVIEW, easeFactor = 1f)
        setupRepository()

        val result = useCase(card, ReviewButton.AGAIN)

        assertEquals(CardState.LAPSE, result.cardState)
        assertEquals(1.3f, result.easeFactor, 0.001f)
    }

    @Test
    fun `easeFactor does not exceed MAX_EASE_FACTOR on EASY`() = runTest {
        val card = newCard().copy(cardState = CardState.REVIEW, easeFactor = 3.5f)
        setupRepository()

        val result = useCase(card, ReviewButton.EASY)

        assertEquals(CardState.REVIEW, result.cardState)
        assertEquals(3.5f, result.easeFactor, 0.001f)
    }

    @Test
    fun `LAPSE + AGAIN keeps card in LAPSE state`() = runTest {
        val card = newCard().copy(cardState = CardState.LAPSE)
        setupRepository()

        val result = useCase(card, ReviewButton.AGAIN)

        assertEquals(CardState.LAPSE, result.cardState)
    }

    @Test
    fun `LAPSE + GOOD graduates to REVIEW with interval coerced to minimum 1f`() = runTest {
        val card = newCard().copy(cardState = CardState.LAPSE, learningStep = 2)
        setupRepository()

        val result = useCase(card, ReviewButton.GOOD)

        assertEquals(CardState.REVIEW, result.cardState)
        assertEquals(1f, result.interval, 0.001f)
        assertEquals(1, result.repetitionCount)
        assertEquals(DAY_MS * 11, result.nextReviewDate)
    }

    @Test
    fun `LAPSE + EASY graduates to REVIEW with interval coerced to minimum 4f and increased easeFactor`() =
        runTest {
            val card = newCard().copy(cardState = CardState.LAPSE)
            setupRepository()

            val result = useCase(card, ReviewButton.EASY)

            assertEquals(CardState.REVIEW, result.cardState)
            assertEquals(4f, result.interval, 0.001f)
            assertEquals(2.6f, result.easeFactor, 0.001f)
            assertEquals(1, result.repetitionCount)
            assertEquals(DAY_MS * 14, result.nextReviewDate)
        }

    @Test
    fun `REVIEW + AGAIN transitions to LAPSE and increments lapseCount`() = runTest {
        val card = newCard().copy(cardState = CardState.REVIEW)
        setupRepository()

        val result = useCase(card, ReviewButton.AGAIN)

        assertEquals(CardState.LAPSE, result.cardState)
        assertEquals(1, result.lapseCount)
    }

    @Test
    fun `firstReviewDate is set on first review and not overwritten on subsequent reviews`() =
        runTest {
            val card = newCard().copy(cardState = CardState.REVIEW, firstReviewDate = now)
            setupRepository()

            val result = useCase(card, ReviewButton.AGAIN)

            assertEquals(card.firstReviewDate, result.firstReviewDate)
        }

    @Test
    fun `lastReviewDate is updated to current time on each review`() = runTest {
        val card = newCard().copy(cardState = CardState.REVIEW, lastReviewDate = now - 1000L)
        setupRepository()

        val result = useCase(card, ReviewButton.AGAIN)

        assertEquals(now, result.lastReviewDate)
    }

    @Test
    fun `previewNextInterval for NEW + AGAIN returns 1 minute`() {
        val card = newCard()
        val result = useCase.previewNextInterval(card, ReviewButton.AGAIN)

        assertEquals(1.minutes.inWholeMilliseconds, result)
    }

    @Test
    fun `previewNextInterval for NEW + HARD returns 1 minute`() {
        val card = newCard()
        val result = useCase.previewNextInterval(card, ReviewButton.HARD)

        assertEquals(1.minutes.inWholeMilliseconds, result)
    }

    @Test
    fun `previewNextInterval for NEW + GOOD returns 10 minutes`() {
        val card = newCard()
        val result = useCase.previewNextInterval(card, ReviewButton.GOOD)

        assertEquals(10.minutes.inWholeMilliseconds, result)
    }

    @Test
    fun `previewNextInterval for NEW + EASY returns interval until next day boundary`() {
        val card = newCard()
        val result = useCase.previewNextInterval(card, ReviewButton.EASY)

        assertEquals(3.days.inWholeMilliseconds + 12.hours.inWholeMilliseconds, result)
    }
}
