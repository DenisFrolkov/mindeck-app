package com.mindeck.app.navigation

import androidx.compose.foundation.layout.PaddingValues
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
import com.mindeck.feature.card.CreateCardNavigationEvent
import com.mindeck.feature.card.CreateCardScreen
import com.mindeck.feature.home.HomeNavigationEvent
import com.mindeck.feature.home.HomeScreen
import com.mindeck.feature.home.showsAddFab

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
                    ChildContent(
                        child = child.instance,
                        rootComponent = rootComponent,
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
    contentPadding: PaddingValues,
) {
    when (child) {
        is Child.Home ->
            HomeContent(
                child = child,
                onNavigate = { event -> homeDestinationFor(event)?.let(rootComponent::push) },
                contentPadding = contentPadding,
            )

        is Child.CreateCard ->
            CreateCardContent(
                child = child,
                onNavigate = { event -> createCardDestinationFor(event)?.let(rootComponent::push) },
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
    contentPadding: PaddingValues,
) {
    val state by child.viewModel.state.collectAsState()

    CreateCardScreen(
        state = state,
        onIntent = child.viewModel::accept,
        onNavigate = onNavigate,
        contentPadding = contentPadding,
    )
}

private fun homeDestinationFor(event: HomeNavigationEvent): Config? =
    when (event) {
        HomeNavigationEvent.CreateCard -> Config.CreateCard
        HomeNavigationEvent.Search,
        HomeNavigationEvent.Settings,
        HomeNavigationEvent.ImportDeck,
        HomeNavigationEvent.Review,
        HomeNavigationEvent.Statistics,
        HomeNavigationEvent.AllDecks,
        is HomeNavigationEvent.OpenDeck,
        -> null
    }

private fun createCardDestinationFor(event: CreateCardNavigationEvent): Config? =
    when (event) {
        CreateCardNavigationEvent.Back -> Config.Home
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
