package com.mindeck.presentation.viewmodel

import app.cash.turbine.test
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.CardType
import com.mindeck.domain.usecases.card.command.CreateCardUseCase
import com.mindeck.domain.usecases.card.command.SaveAudioUseCase
import com.mindeck.domain.usecases.card.command.SaveImageFromFileUseCase
import com.mindeck.domain.usecases.card.command.SaveImageFromUrlUseCase
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CreateCardFormState
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.util.MainDispatcherRule
import com.mindeck.presentation.viewmodel.card.CreationCardNavigationEvent
import com.mindeck.presentation.viewmodel.card.CreationCardViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreationCardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val createCardUseCase: CreateCardUseCase = mockk()
    private val getAllDecksUseCase: GetAllDecksUseCase = mockk()
    private val createDeckUseCase: CreateDeckUseCase = mockk()
    private val saveImageFromFileUseCase: SaveImageFromFileUseCase = mockk()
    private val saveImageFromUrlUseCase: SaveImageFromUrlUseCase = mockk()
    private val saveAudioUseCase: SaveAudioUseCase = mockk()

    private fun createViewModel(): CreationCardViewModel {
        every { getAllDecksUseCase() } returns flowOf(emptyList())
        return CreationCardViewModel(
            createCardUseCase = createCardUseCase,
            getAllDecksUseCase = getAllDecksUseCase,
            createDeckUseCase = createDeckUseCase,
            saveImageFromFileUseCase = saveImageFromFileUseCase,
            saveImageFromUrlUseCase = saveImageFromUrlUseCase,
            saveAudioUseCase = saveAudioUseCase,
        )
    }

    private fun validForm() = CreateCardFormState(
        title = "Title",
        tag = "",
        selectedDeckId = 1,
        selectedType = CardType.SIMPLE,
    )

    private fun invalidForm() = CreateCardFormState(
        title = "",
        tag = "",
        selectedDeckId = 1,
        selectedType = CardType.SIMPLE,
    )

    @Test
    fun updateForm_updatesFormState() {
        val viewModel = createViewModel()
        viewModel.updateForm { validForm() }

        assertEquals(validForm(), viewModel.formState.value)
    }

    @Test
    fun setDeckId_updatesDeckIdAndClosesModal() {
        val viewModel = createViewModel()
        viewModel.showDeckModal()
        viewModel.setDeckId(1)

        assertEquals(1, viewModel.formState.value.selectedDeckId)
        assertEquals(ModalState.None, viewModel.modalState.value)
    }

    @Test
    fun saveImage_updates_cardImagePath_in_formState() = runTest {
        coEvery { saveImageFromFileUseCase(any()) } returns "/files/image.jpg"

        val viewModel = createViewModel()
        val uri = "content://media/image.jpg"
        viewModel.saveImage(uri)

        assertEquals("/files/image.jpg", viewModel.formState.value.cardImagePath)
    }

    @Test
    fun saveImageFromUrl_updates_cardImagePath_in_formState() = runTest {
        coEvery { saveImageFromUrlUseCase(any()) } returns "/files/image.jpg"

        val viewModel = createViewModel()
        val url = "test_url"
        viewModel.saveImageFromUrl(url)

        assertEquals("/files/image.jpg", viewModel.formState.value.cardImagePath)
    }

    @Test
    fun createDeck_success_setsDeckIdAndClosesModal() = runTest {
        coEvery { createDeckUseCase(any()) } returns 1
        val viewModel = createViewModel()
        viewModel.showDeckModal()

        viewModel.createDeck("Deck Name")
        advanceUntilIdle()

        assertEquals(1, viewModel.formState.value.selectedDeckId)
        assertEquals(ModalState.None, viewModel.modalState.value)
    }

    @Test
    fun createCard_invalidFormDoesNotLockMutex_secondCallSucceeds() = runTest {
        coEvery { createCardUseCase(any()) } just Runs
        val viewModel = createViewModel()
        viewModel.updateForm { invalidForm() }

        viewModel.createCard("Question?", "Answer")
        advanceUntilIdle()

        assertEquals(UiState.Idle, viewModel.createCardState.value)

        viewModel.updateForm { validForm() }

        viewModel.navigationEvent.test {
            viewModel.createCard("Question?", "Answer")
            advanceUntilIdle()

            assertEquals(UiState.Success(Unit), viewModel.createCardState.value)
            assertEquals(
                CreationCardNavigationEvent.ShowToast(R.string.toast_card_created_successfully),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun createCard_success_updatesStateEmitsToastAndClearsForm() = runTest {
        coEvery { createCardUseCase(any()) } just Runs
        val viewModel = createViewModel()
        viewModel.updateForm { validForm() }

        viewModel.navigationEvent.test {
            viewModel.createCard("Question?", "Answer")
            advanceUntilIdle()

            assertEquals(UiState.Success(Unit), viewModel.createCardState.value)
            assertEquals(
                CreateCardFormState(selectedDeckId = 1, selectedType = CardType.SIMPLE),
                viewModel.formState.value,
            )
            assertEquals(
                CreationCardNavigationEvent.ShowToast(R.string.toast_card_created_successfully),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun createCard_invalidForm_stateRemainsIdle() = runTest {
        val viewModel = createViewModel()

        viewModel.createCard("Question?", "Answer")
        advanceUntilIdle()

        assertEquals(UiState.Idle, viewModel.createCardState.value)
    }

    @Test
    fun createCard_nameAlreadyExists_emitsError() = runTest {
        coEvery { createCardUseCase(any()) } throws DomainError.NameAlreadyExists()
        val viewModel = createViewModel()
        viewModel.updateForm { validForm() }

        viewModel.createCard("Question?", "Answer")
        advanceUntilIdle()

        assertEquals(
            UiState.Error(
                R.string.error_card_name_already_exists,
            ),
            viewModel.createCardState.value,
        )
    }

    @Test
    fun createCard_databaseError_emitsError() = runTest {
        coEvery { createCardUseCase(any()) } throws DomainError.DatabaseError()
        val viewModel = createViewModel()
        viewModel.updateForm { validForm() }

        viewModel.createCard("Question?", "Answer")
        advanceUntilIdle()

        assertEquals(
            UiState.Error(
                R.string.error_failed_to_create_card,
            ),
            viewModel.createCardState.value,
        )
    }
}
