package com.mindeck.feature.home

import com.mindeck.feature.home.model.DailyReviewUi
import com.mindeck.feature.home.model.DeckItem

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState

    data object Empty : HomeUiState

    data class Content(
        val dailyReview: DailyReviewUi = DailyReviewUi.Pending(),
        val decks: List<DeckItem> = emptyList(),
    ) : HomeUiState
}

val HomeUiState.showsAddFab: Boolean
    get() = this is HomeUiState.Content

sealed interface HomeIntent {
    data object Retry : HomeIntent
}

sealed interface HomeNavigationEvent {
    data object Search : HomeNavigationEvent

    data object Settings : HomeNavigationEvent

    data object CreateCard : HomeNavigationEvent

    data object ImportDeck : HomeNavigationEvent

    data object Review : HomeNavigationEvent

    data object Statistics : HomeNavigationEvent

    data object AllDecks : HomeNavigationEvent

    data class OpenDeck(
        val deckId: Int,
    ) : HomeNavigationEvent
}
