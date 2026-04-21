package com.mindeck.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showSystemUi = true)
@Composable
private fun DecksScreenContentPreview() {
    MindeckTheme {
        DecksScreenContent(
            decksState = UiState.Success(
                listOf(
                    Deck(deckId = 1, deckName = "Английский язык"),
                    Deck(deckId = 2, deckName = "Математика"),
                    Deck(deckId = 3, deckName = "История"),
                ),
            ),
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DecksScreenContentLoadingPreview() {
    MindeckTheme {
        DecksScreenContent(
            decksState = UiState.Loading,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DecksScreenContentEmptyPreview() {
    MindeckTheme {
        DecksScreenContent(
            decksState = UiState.Success(emptyList()),
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DecksScreenContentErrorPreview() {
    MindeckTheme {
        DecksScreenContent(
            decksState = UiState.Error(R.string.error_get_all_decks),
            actions = previewActions,
        )
    }
}

private val previewActions = DecksScreenActions(
    onNavigateBack = {},
    onNavigateToDeck = {},
)
