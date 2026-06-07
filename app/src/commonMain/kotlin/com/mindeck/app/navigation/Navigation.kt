package com.mindeck.app.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mindeck.core.ui.button.AppFAB
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.home.SecondScreen
import com.mindeck.feature.home.home.HomeScreen
import com.mindeck.feature.home.home.showsAddFab

@Composable
fun Navigation(
    rootComponent: RootComponent,
    modifier: Modifier = Modifier,
) {
    val stack by rootComponent.stack.subscribeAsState()

    MindeckTheme {
        CompositionLocalProvider(LocalRootComponent provides rootComponent) {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0),
                floatingActionButton = {
                    val fab = fabConfigFor(stack.active.instance, rootComponent::push)
                    if (fab != null) {
                        AppFAB(onClick = fab.onClick)
                    }
                },
            ) { paddingValues ->
                Children(stack = stack) { child ->
                    when (val instance = child.instance) {
                        is Child.Main -> {
                            val state by instance.viewModel.state.collectAsState()
                            HomeScreen(
                                state = state,
                                onIntent = instance.viewModel::accept,
                                onNavigateToSecond = { rootComponent.push(Config.Second) },
                                contentPadding = paddingValues,
                            )
                        }

                        is Child.Second ->
                            SecondScreen(
                                onBack = { rootComponent.pop() },
                                contentPadding = paddingValues,
                            )
                    }
                }
            }
        }
    }
}

private data class FabConfig(
    val onClick: () -> Unit,
)

@Composable
private fun fabConfigFor(
    active: Child,
    onNavigate: (Config) -> Unit,
): FabConfig? =
    when (active) {
        is Child.Main -> {
            val state by active.viewModel.state.collectAsState()
            if (state.showsAddFab) FabConfig(onClick = { onNavigate(Config.Second) }) else null
        }

        Child.Second -> null
    }
