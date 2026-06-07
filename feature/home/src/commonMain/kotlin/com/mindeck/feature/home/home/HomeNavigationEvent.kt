package com.mindeck.feature.home.home

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
