package com.mindeck.presentation.ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalRootComponent = staticCompositionLocalOf<RootComponent> {
    error("RootComponent not provided")
}
