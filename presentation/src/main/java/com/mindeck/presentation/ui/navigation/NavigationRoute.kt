package com.mindeck.presentation.ui.navigation

sealed class NavigationRoute(val route: String) {
    object MainScreen : NavigationRoute("main_screen")
    object CreationCardScreen : NavigationRoute("creation_card_screen")
    object FoldersScreen : NavigationRoute("folders_screen")
    object FolderScreen : NavigationRoute("folder_screen")
}