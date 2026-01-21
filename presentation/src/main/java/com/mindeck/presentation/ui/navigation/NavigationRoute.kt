package com.mindeck.presentation.ui.navigation

sealed class NavigationRoute(val route: String) {
    object MainScreen : NavigationRoute("main_screen")
    object CreationCardScreen : NavigationRoute("creation_card_screen?deckId={deckId}") {
        fun createRoute(deckId: Int? = null): String {
            return if (deckId != null) {
                "creation_card_screen?deckId=$deckId"
            } else {
                "creation_card_screen"
            }
        }
    }
    object DecksScreen : NavigationRoute("decks_screen")
    object DeckScreen : NavigationRoute("deck_screen/{deckId}") {
        fun createRoute(deckId: Int): String = "deck_screen/$deckId"
    }
    object CardScreen : NavigationRoute("card_screen/{cardId}") {
        fun createRoute(cardId: Int): String = "card_screen/$cardId"
    }
    object CardStudyScreen : NavigationRoute("card_study_screen/{cardId}") {
        fun createRoute(cardId: Int): String = "card_study_screen/$cardId"
    }
    object RepeatCardsScreen : NavigationRoute("card_study_screen")
}
