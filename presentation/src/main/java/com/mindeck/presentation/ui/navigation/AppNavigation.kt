package com.mindeck.presentation.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindeck.presentation.ui.screens.CreationCardScreen
import com.mindeck.presentation.ui.screens.FolderScreen
import com.mindeck.presentation.ui.screens.FoldersScreen
import com.mindeck.presentation.ui.screens.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var buttonPosition by remember { mutableStateOf(IntOffset.Zero) }

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.FolderScreen.route
    ) {
        composable(NavigationRoute.MainScreen.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) }) {
            MainScreen(navController, onButtonPositioned = { buttonPosition = it })
        }
        composable(
            NavigationRoute.CreationCardScreen.route,
            enterTransition = {
                scaleIn(
                    initialScale = 0.1f,
                    animationSpec = tween(100)
                ) + fadeIn(animationSpec = tween(100)) + slideIn(
                    initialOffset = {buttonPosition},
                    animationSpec = tween(100)
                )
            },
            exitTransition = {
                scaleOut(
                    targetScale = 0.1f,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300)) + slideOut(
                    targetOffset = {buttonPosition},
                    animationSpec = tween(300)
                )
            }
        ) {
            CreationCardScreen(navController)
        }
        composable(NavigationRoute.FolderScreen.route) {
            FolderScreen()
        }
    }
}