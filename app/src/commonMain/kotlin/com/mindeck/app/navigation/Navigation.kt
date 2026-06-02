package com.mindeck.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mindeck.app.ui.screen.MainScreen
import com.mindeck.app.ui.screen.SecondScreen

@Composable
fun Navigation(rootComponent: RootComponent, modifier: Modifier = Modifier) {
    val stack by rootComponent.stack.subscribeAsState()

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        CompositionLocalProvider(LocalRootComponent provides rootComponent) {
            Children(stack = stack) { child ->
                when (child.instance) {
                    is Child.Main -> MainScreen()
                    is Child.Second -> SecondScreen()
                }
            }
        }
    }
}
