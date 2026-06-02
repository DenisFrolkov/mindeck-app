package com.mindeck.app

import androidx.compose.ui.window.ComposeUIViewController
import com.mindeck.app.navigation.Navigation
import com.mindeck.app.navigation.RootComponent

fun MainViewController(rootComponent: RootComponent) = ComposeUIViewController {
    Navigation(rootComponent)
}
