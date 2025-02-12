package com.mindeck.presentation.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mindeck.presentation.ui.screens.CardScreen
import com.mindeck.presentation.ui.screens.CardStudyScreen
import com.mindeck.presentation.ui.screens.CreationCardScreen
import com.mindeck.presentation.ui.screens.DeckScreen
import com.mindeck.presentation.ui.screens.FolderScreen
import com.mindeck.presentation.ui.screens.FoldersScreen
import com.mindeck.presentation.ui.screens.MainScreen
import com.mindeck.presentation.viewmodel.CardViewModel
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
    cardViewModel: CardViewModel,
    creationCardViewModel: CreationCardViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = NavigationRoute.MainScreen.route
    ) {
        composable(NavigationRoute.MainScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) }
        ) {
            MainScreen(
                navController = navController
            )
        }
        composable(NavigationRoute.CreationCardScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(150)) },
            exitTransition = { fadeOut(animationSpec = tween(150)) }
        ) {
            CreationCardScreen(
                navController = navController
            )
        }
        composable(NavigationRoute.FoldersScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(150)) },
            exitTransition = { fadeOut(animationSpec = tween(150)) }
        ) {
            FoldersScreen(navController = navController)
        }
        composable(
            NavigationRoute.FolderScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            arguments = listOf(navArgument("folderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val folderId = backStackEntry.arguments?.getInt("folderId")
            if (folderId != null) {
                FolderScreen(
                    navController = navController,
                    folderId = folderId
                )
            } else {
                navController.popBackStack()
            }
        }
        composable(
            NavigationRoute.DeckScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId")
            if (deckId != null) {
                DeckScreen(
                    navController = navController,
                    deckId = deckId
                )
            } else {
                navController.popBackStack()
            }
        }
        composable(
            NavigationRoute.CardScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            arguments = listOf(navArgument("cardId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getInt("cardId")
            if (cardId != null) {
                CardScreen(
                    navController = navController,
                    cardId = cardId
                )
            } else {
                navController.popBackStack()
            }
        }
        composable(NavigationRoute.CardStudyScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) }
        ) {
            CardStudyScreen(navController = navController)
        }
    }
}