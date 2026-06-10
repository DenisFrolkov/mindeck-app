package com.mindeck.feature.home.home

import com.mindeck.domain.models.DeckColor

internal object HomePreviewData {
    val decks =
        listOf(
            DeckItem(id = 1, title = "Испанский базовая лексика", deckColor = DeckColor.BLUE, cardCount = 124, newCount = 10, newReviewCount = 5, reviewCount = 3),
            DeckItem(id = 2, title = "Английский фразовые глаголы", deckColor = DeckColor.BLUE, cardCount = 124, newCount = 10, newReviewCount = 5, reviewCount = 3),
            DeckItem(id = 3, title = "Биология термины", deckColor = DeckColor.BLUE, cardCount = 124, newCount = 10, newReviewCount = 5, reviewCount = 3),
        )

    val pendingContent =
        HomeUiState.Content(
            dailyReview = DailyReviewUi.Pending(totalCount = 40, repeatedCount = 1, newCount = 7, newReviewCount = 12, reviewCount = 21),
            decks = decks,
        )

    val completedContent =
        HomeUiState.Content(
            dailyReview = DailyReviewUi.Completed,
            decks = decks,
        )

    val loading =
        HomeUiState.Loading

    val error = HomeUiState.Error("Не удалось загрузить колоды")

    val empty = HomeUiState.Empty
}
