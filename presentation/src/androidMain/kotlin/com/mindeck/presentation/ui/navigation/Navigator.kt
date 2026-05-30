package com.mindeck.presentation.ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("LocalNavigator not provided")
}

interface Navigator {
    fun push(route: NavigationRoute)
    fun pop()
}
