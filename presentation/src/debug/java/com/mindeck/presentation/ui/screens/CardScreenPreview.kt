package com.mindeck.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showSystemUi = true)
@Composable
private fun CardScreenContentPreview() {
    MindeckTheme {
        CardScreenContent(
            cardWithDeckState = UiState.Success(
                CardWithDeck(card = previewCard, deckId = 1, deckName = "Английский язык"),
            ),
            deleteCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CardScreenContentLoadingPreview() {
    MindeckTheme {
        CardScreenContent(
            cardWithDeckState = UiState.Loading,
            deleteCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CardScreenContentErrorPreview() {
    MindeckTheme {
        CardScreenContent(
            cardWithDeckState = UiState.Error(R.string.error_get_info_about_card),
            deleteCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

private val previewActions = CardScreenActions(
    onBack = {},
    onMenuClick = {},
    onDismissModal = {},
    onShowDeleteDialog = {},
    onDeleteCard = {},
    onStudyCard = {},
)

private val previewCard = Card(
    cardName = "Kotlin корутины",
    cardQuestion = "Что такое coroutine?",
    cardAnswer = "Лёгковесная сопрограмма для асинхронного кода в Kotlin",
    cardType = CardType.SIMPLE,
    cardTag = "kotlin",
    deckId = 1,
)
