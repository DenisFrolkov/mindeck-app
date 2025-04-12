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
import com.mindeck.presentation.ui.screens.DecksScreen
import com.mindeck.presentation.ui.screens.MainScreen

@Composable
fun AppNavigation() {
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
        composable(NavigationRoute.DecksScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(150)) },
            exitTransition = { fadeOut(animationSpec = tween(150)) }
        ) {
            DecksScreen(navController = navController)
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
        composable(
            NavigationRoute.CardStudyScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            arguments = listOf(navArgument("cardId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getInt("cardId")
            if (cardId != null) {
                CardStudyScreen(navController = navController, cardId = cardId)
            }
        }
        composable(
            NavigationRoute.RepeatCardsScreen.route,
        ) {
            CardStudyScreen(navController = navController)
        }
    }
}