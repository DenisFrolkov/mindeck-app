package com.mindeck.presentation.viewmodel

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.state.SessionSummary
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAllDecksUseCase: GetAllDecksUseCase = mockk()
    private val getCardsRepetitionUseCase: GetCardsRepetitionUseCase = mockk()

    private fun createViewModel(): MainViewModel {
        every { getAllDecksUseCase() } returns flowOf(emptyList())
        return MainViewModel(getAllDecksUseCase, getCardsRepetitionUseCase)
    }

    private fun card(cardState: CardState) = Card(
        cardName = "Card",
        cardQuestion = "Q?",
        cardAnswer = "A",
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
        cardState = cardState,
    )

    @Test
    fun sessionSummaryState_classifiesLapseAsLearning() = runTest {
        val cards = listOf(
            card(CardState.NEW),
            card(CardState.LEARNING),
            card(CardState.LAPSE),
            card(CardState.REVIEW),
        )
        every { getCardsRepetitionUseCase() } returns flowOf(cards)
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sessionSummaryState.collect {}
        }
        advanceUntilIdle()

        assertEquals(
            UiState.Success(SessionSummary(newCount = 1, learningCount = 2, reviewCount = 1)),
            viewModel.sessionSummaryState.value,
        )
    }
}
