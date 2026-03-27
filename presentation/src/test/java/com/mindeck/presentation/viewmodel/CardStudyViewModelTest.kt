package com.mindeck.presentation.viewmodel

import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.ReviewButton
import com.mindeck.domain.usecases.card.command.UpdateCardReviewUseCase
import com.mindeck.domain.usecases.card.query.GetCardByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardStudyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase = mockk()
    private val getCardByIdUseCase: GetCardByIdUseCase = mockk()
    private val updateCardReviewUseCase: UpdateCardReviewUseCase = mockk()

    private fun createViewModel(): CardStudyViewModel = CardStudyViewModel(
        getCardsRepetitionUseCase = getCardsRepetitionUseCase,
        getCardByIdUseCase = getCardByIdUseCase,
        updateCardReviewUseCase = updateCardReviewUseCase,
    )

    private fun card(cardId: Int = 1, cardState: CardState = CardState.NEW) = Card(
        cardId = cardId,
        cardName = "Card $cardId",
        cardQuestion = "Q?",
        cardAnswer = "A",
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
        cardState = cardState,
    )

    @Test
    fun loadCardRepetition_success_populatesQueue() = runTest {
        val cards = listOf(card(1), card(2))
        every { getCardsRepetitionUseCase() } returns flowOf(cards)
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 1_000L
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()

        assertEquals(UiState.Success(cards), viewModel.cardsState.value)
    }

    @Test
    fun loadCardRepetition_returnsError_whenFlowIsEmpty() = runTest {
        every { getCardsRepetitionUseCase() } returns emptyFlow()
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()

        assertEquals(UiState.Error(R.string.error_get_card_for_study), viewModel.cardsState.value)
    }

    @Test
    fun loadCardRepetition_databaseError_emitsError() = runTest {
        every { getCardsRepetitionUseCase() } throws DomainError.DatabaseError()
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()

        assertEquals(UiState.Error(R.string.error_get_card_for_study), viewModel.cardsState.value)
    }

    @Test
    fun loadCardById_success_populatesQueue() = runTest {
        val card = card(1)
        every { getCardByIdUseCase(any()) } returns flowOf(card)
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 1_000L
        val viewModel = createViewModel()

        viewModel.loadCardById(1)
        advanceUntilIdle()

        assertEquals(UiState.Success(listOf(card)), viewModel.cardsState.value)
    }

    @Test
    fun loadCardById_returnsError_whenCardIsNull() = runTest {
        every { getCardByIdUseCase(any()) } returns flowOf(null)
        val viewModel = createViewModel()

        viewModel.loadCardById(1)
        advanceUntilIdle()

        assertEquals(UiState.Error(R.string.error_failed_to_load_card), viewModel.cardsState.value)
    }

    @Test
    fun reviewCard_learningState_reAddsCardToEndOfQueue() = runTest {
        val card1 = card(1)
        val card2 = card(2)
        val reviewedCard = card(1, CardState.LEARNING)
        every { getCardsRepetitionUseCase() } returns flowOf(listOf(card1, card2))
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 1_000L
        coEvery { updateCardReviewUseCase(card1, ReviewButton.AGAIN) } returns reviewedCard
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()
        viewModel.reviewCard(card1, ReviewButton.AGAIN)
        advanceUntilIdle()

        assertEquals(UiState.Success(listOf(card2, reviewedCard)), viewModel.cardsState.value)
    }

    @Test
    fun reviewCard_lapseState_reAddsCardToEndOfQueue() = runTest {
        val card1 = card(1)
        val card2 = card(2)
        val reviewedCard = card(1, CardState.LAPSE)
        every { getCardsRepetitionUseCase() } returns flowOf(listOf(card1, card2))
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 1_000L
        coEvery { updateCardReviewUseCase(card1, ReviewButton.AGAIN) } returns reviewedCard
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()
        viewModel.reviewCard(card1, ReviewButton.AGAIN)
        advanceUntilIdle()

        assertEquals(UiState.Success(listOf(card2, reviewedCard)), viewModel.cardsState.value)
    }

    @Test
    fun reviewCard_reviewState_removesCardFromQueue() = runTest {
        val card1 = card(1)
        val card2 = card(2)
        val reviewedCard = card(1, CardState.REVIEW)
        every { getCardsRepetitionUseCase() } returns flowOf(listOf(card1, card2))
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 1_000L
        coEvery { updateCardReviewUseCase(card1, ReviewButton.GOOD) } returns reviewedCard
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()
        viewModel.reviewCard(card1, ReviewButton.GOOD)
        advanceUntilIdle()

        assertEquals(UiState.Success(listOf(card2)), viewModel.cardsState.value)
    }

    @Test
    fun reviewLabels_showsSeconds_whenIntervalLessThanOneMinute() = runTest {
        every { getCardsRepetitionUseCase() } returns flowOf(listOf(card()))
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 30_000L
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()

        assertEquals("30 сек", viewModel.reviewLabels.value[ReviewButton.AGAIN])
    }

    @Test
    fun reviewLabels_showsMinutes_whenIntervalLessThanOneHour() = runTest {
        every { getCardsRepetitionUseCase() } returns flowOf(listOf(card()))
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 600_000L
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()

        assertEquals("10 мин", viewModel.reviewLabels.value[ReviewButton.AGAIN])
    }

    @Test
    fun reviewLabels_showsDays_whenIntervalOneHourOrMore() = runTest {
        every { getCardsRepetitionUseCase() } returns flowOf(listOf(card()))
        every { updateCardReviewUseCase.previewNextInterval(any(), any()) } returns 86_400_000L
        val viewModel = createViewModel()

        viewModel.loadCardRepetition()
        advanceUntilIdle()

        assertEquals("1 д", viewModel.reviewLabels.value[ReviewButton.AGAIN])
    }
}
