package com.mindeck.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.DeckScreenData

@Preview(showSystemUi = true)
@Composable
private fun DeckScreenContentPreview() {
    MindeckTheme {
        DeckScreenContent(
            screenUiState = UiState.Success(
                DeckScreenData(
                    deck = previewDeck,
                    cards = listOf(previewCard, previewCard.copy(cardId = 2, cardName = "StateFlow")),
                ),
            ),
            renameDeckState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DeckScreenContentLoadingPreview() {
    MindeckTheme {
        DeckScreenContent(
            screenUiState = UiState.Loading,
            renameDeckState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DeckScreenContentErrorPreview() {
    MindeckTheme {
        DeckScreenContent(
            screenUiState = UiState.Error(R.string.error_get_cards_by_deck_id),
            renameDeckState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

private val previewActions = DeckScreenActions(
    onMenuClick = {},
    onDismissModal = {},
    onShowRenameDialog = {},
    onDeleteDeck = {},
    onRenameDeck = { _, _ -> },
    onNavigateBack = {},
    onNavigateToCard = {},
    onNavigateToCreateCard = {},
)

private val previewDeck = Deck(deckId = 1, deckName = "Английский язык")

private val previewCard = Card(
    cardName = "Kotlin корутины",
    cardQuestion = "Что такое coroutine?",
    cardAnswer = "Лёгковесная сопрограмма для асинхронного кода",
    cardType = CardType.SIMPLE,
    cardTag = "",
    deckId = 1,
)
