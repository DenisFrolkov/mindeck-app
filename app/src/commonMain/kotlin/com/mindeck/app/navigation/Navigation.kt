package com.mindeck.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.home.SecondScreen
import com.mindeck.feature.home.home.HomeScreen

@Composable
fun Navigation(
    rootComponent: RootComponent,
    modifier: Modifier = Modifier,
) {
    val stack by rootComponent.stack.subscribeAsState()

    MindeckTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            CompositionLocalProvider(LocalRootComponent provides rootComponent) {
                Children(stack = stack) { child ->
                    when (val instance = child.instance) {
                        is Child.Main -> {
                            val state by instance.viewModel.state.collectAsState()
                            HomeScreen(
                                state = state,
                                onIntent = instance.viewModel::accept,
                                onNavigateToSecond = { rootComponent.push(Config.Second) },
                            )
                        }
                        is Child.Second -> SecondScreen(onBack = { rootComponent.pop() })
                    }
                }
            }
        }
    }
}
