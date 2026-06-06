package com.mindeck.feature.home.home

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState

    data object Empty : HomeUiState

    data class Content(
        val dailyReview: DailyReviewUi,
        val decks: List<DeckUi>,
    ) : HomeUiState
}
