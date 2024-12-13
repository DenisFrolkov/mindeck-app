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
    object CardStudyScreen : NavigationRoute("card_study_screen/{folderId}") {
        fun createRoute(folderId: Int): String = "folder_screen/$folderId"
    }
}