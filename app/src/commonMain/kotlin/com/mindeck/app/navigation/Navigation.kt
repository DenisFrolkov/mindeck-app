package com.mindeck.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mindeck.core.ui.button.AppFAB
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.card.createCard.CreateCardNavigationEvent
import com.mindeck.feature.card.createCard.CreateCardScreen
import com.mindeck.feature.home.HomeNavigationEvent
import com.mindeck.feature.home.HomeScreen
import com.mindeck.feature.home.showsAddFab

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun Navigation(
    rootComponent: RootComponent,
    modifier: Modifier = Modifier,
) {
    val stack by rootComponent.stack.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    MindeckTheme {
        CompositionLocalProvider(LocalRootComponent provides rootComponent) {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                floatingActionButton = {
                    val fab = fabConfigFor(stack.active.instance, rootComponent::push)
                    if (fab != null) {
                        AppFAB(onClick = fab.onClick)
                    }
                },
            ) { paddingValues ->
                Children(
                    stack = stack,
                    animation =
                        predictiveBackAnimation(
                            backHandler = rootComponent.backHandler,
                            fallbackAnimation = stackAnimation(slide()),
                            onBack = rootComponent::pop,
                        ),
                ) { child ->
                    ChildContent(
                        child = child.instance,
                        rootComponent = rootComponent,
                        snackbarHostState = snackbarHostState,
                        contentPadding = paddingValues,
                    )
                }
            }
        }
    }
}

@Composable
private fun ChildContent(
    child: Child,
    rootComponent: RootComponent,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
) {
    when (child) {
        is Child.Home ->
            HomeContent(
                child = child,
                onNavigate = { rootComponent.navigate(homeDestinationFor(it)) },
                contentPadding = contentPadding,
            )

        is Child.CreateCard ->
            CreateCardContent(
                child = child,
                onNavigate = { rootComponent.navigate(createCardDestinationFor(it)) },
                snackbarHostState = snackbarHostState,
                contentPadding = contentPadding,
            )
    }
}

@Composable
private fun HomeContent(
    child: Child.Home,
    onNavigate: (HomeNavigationEvent) -> Unit,
    contentPadding: PaddingValues,
) {
    val state by child.viewModel.state.collectAsState()
    HomeScreen(
        state = state,
        onIntent = child.viewModel::accept,
        onNavigate = onNavigate,
        contentPadding = contentPadding,
    )
}

@Composable
private fun CreateCardContent(
    child: Child.CreateCard,
    onNavigate: (CreateCardNavigationEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
) {
    val state by child.viewModel.state.collectAsState()
    CreateCardScreen(
        state = state,
        effects = child.viewModel.effects,
        onIntent = child.viewModel::accept,
        onNavigate = onNavigate,
        snackbarHostState = snackbarHostState,
        contentPadding = contentPadding,
    )
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
        is Child.Home -> {
            val state by active.viewModel.state.collectAsState()
            if (state.showsAddFab) FabConfig(onClick = { onNavigate(Config.CreateCard) }) else null
        }

        is Child.CreateCard -> null
    }
