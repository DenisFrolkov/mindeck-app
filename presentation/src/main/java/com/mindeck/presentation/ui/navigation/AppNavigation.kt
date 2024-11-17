package com.mindeck.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindeck.presentation.ui.screens.CreationCardScreen
import com.mindeck.presentation.ui.screens.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.MainScreen.route
    ) {
        composable(
            NavigationRoute.MainScreen.route
        ) {
            MainScreen()
        }
        composable(
            NavigationRoute.CreationCardScreen.route
        ) {
            CreationCardScreen()
        }
    }
}