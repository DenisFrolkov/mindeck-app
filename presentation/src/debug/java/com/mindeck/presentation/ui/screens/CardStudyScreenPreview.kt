package com.mindeck.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.ReviewButton
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showSystemUi = true)
@Composable
private fun CardStudyScreenContentPreview() {
    MindeckTheme {
        CardStudyScreenContent(
            modalState = ModalState.None,
            cardsForRepetitionState = UiState.Success(listOf(previewCard)),
            reviewLabels = mapOf(
                ReviewButton.AGAIN to 600_000L,
                ReviewButton.HARD to 86_400_000L,
                ReviewButton.GOOD to 259_200_000L,
                ReviewButton.EASY to 604_800_000L,
            ),
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CardStudyScreenContentLoadingPreview() {
    MindeckTheme {
        CardStudyScreenContent(
            modalState = ModalState.None,
            cardsForRepetitionState = UiState.Loading,
            reviewLabels = emptyMap(),
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CardStudyScreenContentErrorPreview() {
    MindeckTheme {
        CardStudyScreenContent(
            modalState = ModalState.None,
            cardsForRepetitionState = UiState.Error(R.string.error_get_card_for_study),
            reviewLabels = emptyMap(),
            actions = previewActions,
        )
    }
}

private val previewActions = CardStudyScreenActions(
    onNavigateBack = {},
    onShowDropdownMenu = {},
    onHideModal = {},
    onReviewCard = { _, _ -> },
)

private val previewCard = Card(
    cardName = "Kotlin корутины",
    cardQuestion = "Что такое coroutine?",
    cardAnswer = "Лёгковесная сопрограмма для асинхронного кода",
    cardType = CardType.SIMPLE,
    cardTag = "",
    deckId = 1,
    cardState = CardState.REVIEW,
)
