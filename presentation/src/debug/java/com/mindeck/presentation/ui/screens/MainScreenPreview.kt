package com.mindeck.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.SessionSummary
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showSystemUi = true)
@Composable
private fun MainScreenContentPreview() {
    MindeckTheme {
        MainScreenContent(
            decksState = UiState.Success(
                listOf(
                    Deck(deckId = 1, deckName = "Английский язык"),
                    Deck(deckId = 2, deckName = "Математика"),
                ),
            ),
            sessionSummaryState = UiState.Success(
                SessionSummary(newCount = 5, learningCount = 3, reviewCount = 12),
            ),
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MainScreenContentLoadingPreview() {
    MindeckTheme {
        MainScreenContent(
            decksState = UiState.Loading,
            sessionSummaryState = UiState.Loading,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MainScreenContentErrorPreview() {
    MindeckTheme {
        MainScreenContent(
            decksState = UiState.Error(R.string.error_get_all_decks),
            sessionSummaryState = UiState.Loading,
            actions = previewActions,
        )
    }
}

private val previewActions = MainScreenActions(
    onNavigateToStudy = {},
    onNavigateToDeck = {},
    onNavigateToDecks = {},
    onNavigateToCreateCard = {},
)
