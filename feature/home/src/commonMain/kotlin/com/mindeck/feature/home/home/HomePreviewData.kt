package com.mindeck.feature.home.home

internal object HomePreviewData {
    val decks =
        listOf(
            DeckUi(id = 1, title = "Испанский базовая лексика", cardCount = 124, reviewCount = 18),
            DeckUi(id = 2, title = "Английский фразовые глаголы", cardCount = 86, reviewCount = 0),
            DeckUi(id = 3, title = "Биология термины", cardCount = 42, reviewCount = 5),
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
