package com.mindeck.presentation.ui.navigation

sealed class NavigationRoute(val route: String) {
    object MainScreen : NavigationRoute("main_screen")
    object CreationCardScreen : NavigationRoute("creation_card_screen")
    object FoldersScreen : NavigationRoute("folders_screen")
    object FolderScreen : NavigationRoute("folder_screen/{folderId}") {
        fun createRoute(folderId: Int): String = "folder_screen/$folderId"
    }
    object DeckScreen : NavigationRoute("deck_screen/{deckId}") {
        fun createRoute(deckId: Int): String = "deck_screen/$deckId"
    }
    object CardScreen : NavigationRoute("card_screen/{cardId}") {
        fun createRoute(cardId: Int): String = "card_screen/$cardId"
    }
    object CardStudyScreen : NavigationRoute("card_study_screen/{cardId}") {
        fun createRoute(cardId: Int): String = "card_study_screen/$cardId"
    }
}