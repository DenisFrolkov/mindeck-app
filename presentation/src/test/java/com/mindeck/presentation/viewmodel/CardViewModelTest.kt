package com.mindeck.presentation.viewmodel

import app.cash.turbine.test
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.usecases.card.command.DeleteCardUseCase
import com.mindeck.domain.usecases.card.query.GetCardWithDeckByIdUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.util.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCardWithDeckByIdUseCase: GetCardWithDeckByIdUseCase = mockk()
    private val deleteCardUseCase: DeleteCardUseCase = mockk()

    private fun createViewModel(): CardViewModel = CardViewModel(
        getCardWithDeckByIdUseCase = getCardWithDeckByIdUseCase,
        deleteCardUseCase = deleteCardUseCase,
    )

    private fun card() = Card(
        cardName = "Card",
        cardQuestion = "Q?",
        cardAnswer = "A",
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
        cardState = CardState.NEW,
    )

    private fun cardWithDeck() = CardWithDeck(
        card = card(),
        deckId = 1,
        deckName = "Deck",
    )

    @Test
    fun cardWithDeck_returnsSuccess_whenCardLoaded() = runTest {
        every { getCardWithDeckByIdUseCase(any()) } returns flowOf(cardWithDeck())
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.cardWithDeck.collect {}
        }
        viewModel.loadCardById(1)
        advanceUntilIdle()

        assertEquals(UiState.Success(cardWithDeck()), viewModel.cardWithDeck.value)
    }

    @Test
    fun cardWithDeck_returnsError_whenCardIsNull() = runTest {
        every { getCardWithDeckByIdUseCase(any()) } returns flowOf(null)
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.cardWithDeck.collect {}
        }
        viewModel.loadCardById(1)
        advanceUntilIdle()

        assertEquals(UiState.Error(R.string.error_failed_to_load_card), viewModel.cardWithDeck.value)
    }

    @Test
    fun cardWithDeck_returnsError_whenFlowThrowsDatabaseError() = runTest {
        every { getCardWithDeckByIdUseCase(any()) } returns flow { throw DomainError.DatabaseError() }
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.cardWithDeck.collect {}
        }
        viewModel.loadCardById(1)
        advanceUntilIdle()

        assertEquals(UiState.Error(R.string.error_failed_to_load_card), viewModel.cardWithDeck.value)
    }

    @Test
    fun deleteCard_success_updatesStateClosesModalAndEmitsEvent() = runTest {
        coEvery { deleteCardUseCase(any()) } just Runs
        val viewModel = createViewModel()
        viewModel.showDeleteDialog()

        viewModel.uiEvent.test {
            viewModel.deleteCard(card())
            advanceUntilIdle()

            assertEquals(UiState.Success(Unit), viewModel.deleteCardState.value)
            assertEquals(ModalState.None, viewModel.modalState.value)
            assertEquals(CardUiEvent.DeletionSuccessful("Card"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteCard_databaseError_emitsError() = runTest {
        coEvery { deleteCardUseCase(any()) } throws DomainError.DatabaseError()
        val viewModel = createViewModel()

        viewModel.deleteCard(card())
        advanceUntilIdle()

        assertEquals(UiState.Error(R.string.error_failed_to_delete_card), viewModel.deleteCardState.value)
    }

    @Test
    fun deleteCard_domainError_emitsError() = runTest {
        coEvery { deleteCardUseCase(any()) } throws DomainError.NameAlreadyExists()
        val viewModel = createViewModel()

        viewModel.deleteCard(card())
        advanceUntilIdle()

        assertEquals(UiState.Error(R.string.error_something_went_wrong), viewModel.deleteCardState.value)
    }
}
