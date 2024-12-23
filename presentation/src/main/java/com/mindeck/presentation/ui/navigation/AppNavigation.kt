package com.mindeck.presentation.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.screens.CardStudyScreen
import com.mindeck.presentation.ui.screens.CreationCardScreen
import com.mindeck.presentation.ui.screens.DeckScreen
import com.mindeck.presentation.ui.screens.FolderScreen
import com.mindeck.presentation.ui.screens.FoldersScreen
import com.mindeck.presentation.ui.screens.MainScreen
import com.mindeck.presentation.viewmodel.CreationCardViewModel
import com.mindeck.presentation.viewmodel.DeckViewModel
import com.mindeck.presentation.viewmodel.FolderViewModel
import com.mindeck.presentation.viewmodel.FoldersViewModel
import com.mindeck.presentation.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel,
    foldersViewModel: FoldersViewModel,
    folderViewModel: FolderViewModel,
    deckViewModel: DeckViewModel,
    creationCardViewModel: CreationCardViewModel
) {
    val navController = rememberNavController()
    var buttonPosition by remember { mutableStateOf(IntOffset.Zero) }

    NavHost(
        navController = navController, startDestination = NavigationRoute.MainScreen.route
    ) {
        composable(NavigationRoute.MainScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)) },
            exitTransition = { fadeOut(animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)) }
        ) {
            MainScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                onButtonPositioned = { buttonPosition = it })
        }
        composable(NavigationRoute.CreationCardScreen.route, enterTransition = {
            scaleIn(
                initialScale = 0.1f, animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)
            ) + fadeIn(animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)) + slideIn(
                initialOffset = { buttonPosition }, animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)
            )
        }, exitTransition = {
            scaleOut(
                targetScale = 0.1f, animationSpec = tween(R.dimen.app_navigation_three_hundred_duration_millis)
            ) + fadeOut(animationSpec = tween(R.dimen.app_navigation_three_hundred_duration_millis)) + slideOut(
                targetOffset = { buttonPosition }, animationSpec = tween(R.dimen.app_navigation_three_hundred_duration_millis)
            )
        }) {
            CreationCardScreen(
                navController = navController,
                creationCardViewModel = creationCardViewModel
            )
        }
        composable(NavigationRoute.FoldersScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(R.dimen.app_navigation_one_hundred_fifty_duration_millis)) },
            exitTransition = { fadeOut(animationSpec = tween(R.dimen.app_navigation_one_hundred_fifty_duration_millis)) }) {
            FoldersScreen(navController = navController, foldersViewModel = foldersViewModel)
        }
        composable(
            NavigationRoute.FolderScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(R.dimen.app_navigation_one_hundred_fifty_duration_millis)) },
            exitTransition = { fadeOut(animationSpec = tween(R.dimen.app_navigation_one_hundred_fifty_duration_millis)) },
            arguments = listOf(navArgument("folderId") { type = NavType.IntType })
        ) {
            backStackEntry ->
            val folderId = backStackEntry.arguments?.getInt("folderId")
            folderViewModel.getFolderById(folderId = folderId!!)
            folderViewModel.getAllDecksByFolderId(folderId = folderId!!)
            FolderScreen(
                navController = navController,
                folderViewModel = folderViewModel,
                deckViewModel = deckViewModel
            )
        }
        composable(NavigationRoute.DeckScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)) },
            exitTransition = { fadeOut(animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)) },
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId")
            deckViewModel.getAllCardsByDeckId(deckId!!)
            DeckScreen(
                navController = navController,
                onButtonPositioned = { buttonPosition = it },
                deckViewModel = deckViewModel
            )
        }
        composable(NavigationRoute.CardStudyScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)) },
            exitTransition = { fadeOut(animationSpec = tween(R.dimen.app_navigation_hundred_duration_millis)) }) {
            CardStudyScreen(navController = navController)
        }
    }
}