package com.mindeck.presentation

import androidx.compose.ui.window.ComposeUIViewController
import com.mindeck.presentation.ui.navigation.Navigation
import com.mindeck.presentation.ui.navigation.RootComponent

fun MainViewController(rootComponent: RootComponent) = ComposeUIViewController {
    Navigation(rootComponent)
}
