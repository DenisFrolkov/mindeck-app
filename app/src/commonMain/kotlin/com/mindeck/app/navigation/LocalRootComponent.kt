package com.mindeck.app.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalRootComponent =
    staticCompositionLocalOf<RootComponent> {
        error("RootComponent not provided")
    }
