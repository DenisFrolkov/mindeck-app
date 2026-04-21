package com.mindeck.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.state.CreateCardFormState
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showSystemUi = true)
@Composable
private fun CreationCardScreenContentEmptyPreview() {
    MindeckTheme {
        CreationCardScreenContent(
            formState = CreateCardFormState(),
            deckState = UiState.Success(
                listOf(
                    Deck(deckId = 1, deckName = "Английский язык"),
                    Deck(deckId = 2, deckName = "Математика"),
                ),
            ),
            createDeckState = UiState.Idle,
            createCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreationCardScreenContentFilledPreview() {
    MindeckTheme {
        CreationCardScreenContent(
            formState = CreateCardFormState(
                title = "Kotlin корутины",
                question = "Что такое coroutine?",
                answer = "Лёгковесная сопрограмма для асинхронного кода",
                selectedDeckId = 1,
                selectedType = CardType.SIMPLE,
            ),
            deckState = UiState.Success(
                listOf(Deck(deckId = 1, deckName = "Английский язык")),
            ),
            createDeckState = UiState.Idle,
            createCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

private val previewActions = CreationCardScreenActions(
    onNavigateBack = {},
    onShowDeckModal = {},
    onShowTypeModal = {},
    onUpdateForm = {},
    onCreateCard = {},
    onHideModal = {},
    onSetDeckId = {},
    onSetType = {},
    onCreateDeck = {},
)
