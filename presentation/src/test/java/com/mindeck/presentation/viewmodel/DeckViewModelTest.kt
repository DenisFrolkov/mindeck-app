package com.mindeck.presentation.viewmodel

import app.cash.turbine.test
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.Deck
import com.mindeck.domain.usecases.card.query.GetAllCardsUseCase
import com.mindeck.domain.usecases.deck.command.DeleteDeckUseCase
import com.mindeck.domain.usecases.deck.command.RenameDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
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
class DeckViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCardsUseCase: GetAllCardsUseCase = mockk()
    private val getDeckByIdUseCase: GetDeckByIdUseCase = mockk()
    private val renameDeckUseCase: RenameDeckUseCase = mockk()
    private val deleteDeckUseCase: DeleteDeckUseCase = mockk()

    private fun createViewModel(): DeckViewModel {
        return DeckViewModel(
            getCardsUseCase = getCardsUseCase,
            getDeckByIdUseCase = getDeckByIdUseCase,
            renameDeckUseCase = renameDeckUseCase,
            deleteDeckUseCase = deleteDeckUseCase,
        )
    }

    private fun deck(deckId: Int, deckName: String) = Deck(
        deckId = deckId,
        deckName = deckName,
    )

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
    fun screenUiState_returnsSuccess_whenDeckAndCardsLoaded() = runTest {
        val deck1 = deck(1, "Deck 1")
        val card1 = card(cardState = CardState.NEW)

        every { getDeckByIdUseCase(any()) } returns flowOf(deck1)
        every { getCardsUseCase(any()) } returns flowOf(listOf(card1))
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.screenUiState.collect {}
        }
        viewModel.loadDeckWithCards(1)
        advanceUntilIdle()

        assertEquals(
            UiState.Success(DeckScreenData(deck1, listOf(card1))),
            viewModel.screenUiState.value,
        )
    }

    @Test
    fun screenUiState_returnsError_whenDeckIsNull() = runTest {
        every { getDeckByIdUseCase(any()) } returns flowOf(null)
        every { getCardsUseCase(any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.screenUiState.collect {}
        }
        viewModel.loadDeckWithCards(1)
        advanceUntilIdle()

        assertEquals(
            UiState.Error(R.string.error_failed_to_load_deck),
            viewModel.screenUiState.value,
        )
    }

    @Test
    fun screenUiState_returnsError_whenFlowThrowsDatabaseError() = runTest {
        every { getDeckByIdUseCase(any()) } returns flow { throw DomainError.DatabaseError() }
        every { getCardsUseCase(any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.screenUiState.collect {}
        }
        viewModel.loadDeckWithCards(1)
        advanceUntilIdle()

        assertEquals(
            UiState.Error(R.string.error_failed_to_load_deck),
            viewModel.screenUiState.value,
        )
    }

    @Test
    fun renameDeck_success_updatesStateAndClosesModal() = runTest {
        coEvery { renameDeckUseCase(any(), any()) } just Runs
        val viewModel = createViewModel()

        viewModel.showRenameDialog()
        viewModel.renameDeck(1, "Deck New")
        advanceUntilIdle()

        assertEquals(UiState.Success(Unit), viewModel.renameDeckState.value)
        assertEquals(ModalState.None, viewModel.modalState.value)
    }

    @Test
    fun renameDeck_error_nameAlreadyExists() = runTest {
        coEvery { renameDeckUseCase(any(), any()) } throws DomainError.NameAlreadyExists()
        val viewModel = createViewModel()

        viewModel.showRenameDialog()
        viewModel.renameDeck(1, "Deck New")
        advanceUntilIdle()

        assertEquals(
            UiState.Error(R.string.error_deck_name_already_exists, listOf("Deck New")),
            viewModel.renameDeckState.value,
        )
        assertEquals(ModalState.RenameDialog, viewModel.modalState.value)
    }

    @Test
    fun renameDeck_error_databaseError() = runTest {
        coEvery { renameDeckUseCase(any(), any()) } throws DomainError.DatabaseError()
        val viewModel = createViewModel()

        viewModel.showRenameDialog()
        viewModel.renameDeck(1, "Deck New")
        advanceUntilIdle()

        assertEquals(
            UiState.Error(R.string.error_database_try_again),
            viewModel.renameDeckState.value,
        )
        assertEquals(ModalState.RenameDialog, viewModel.modalState.value)
    }

    @Test
    fun deleteDeck_success_emitsGoBackAndShowToast() = runTest {
        coEvery { deleteDeckUseCase(any()) } just Runs
        val viewModel = createViewModel()

        viewModel.navigationEvent.test {
            viewModel.deleteDeck(1)
            advanceUntilIdle()

            assertEquals(DeckNavigationEvent.GoBack, awaitItem())
            assertEquals(
                DeckNavigationEvent.ShowToast(R.string.toast_deck_deleted_successfully),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteDeck_error_domainError() = runTest {
        coEvery { deleteDeckUseCase(any()) } throws DomainError.DatabaseError()
        val viewModel = createViewModel()

        viewModel.navigationEvent.test {
            viewModel.deleteDeck(1)
            advanceUntilIdle()

            assertEquals(
                DeckNavigationEvent.ShowToast(R.string.toast_message_impossible_delete_deck),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteDeck_error_exception() = runTest {
        coEvery { deleteDeckUseCase(any()) } throws Exception()
        val viewModel = createViewModel()

        viewModel.navigationEvent.test {
            viewModel.deleteDeck(1)
            advanceUntilIdle()

            assertEquals(
                DeckNavigationEvent.ShowToast(R.string.error_something_went_wrong),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
