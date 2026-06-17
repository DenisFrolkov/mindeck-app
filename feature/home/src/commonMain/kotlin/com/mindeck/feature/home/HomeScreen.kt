package com.mindeck.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindeck.core.ui.appBar.AppBar
import com.mindeck.core.ui.appBar.AppBarAction
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.home.components.HomeContent
import com.mindeck.feature.home.components.HomeEmpty
import com.mindeck.feature.home.components.HomeError
import com.mindeck.feature.home.components.HomeLoading
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.search_icon
import mindeck_app.core.ui.generated.resources.settings_icon
import mindeck_app.feature.home.generated.resources.home_action_search
import mindeck_app.feature.home.generated.resources.home_action_settings
import mindeck_app.feature.home.generated.resources.home_title
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.home.generated.resources.Res as HomeRes

@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    onNavigate: (HomeNavigationEvent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(MindeckTheme.dimensions.screenPadding),
    ) {
        AppBar(
            title = stringResource(HomeRes.string.home_title),
            actions =
                listOf(
                    AppBarAction(
                        icon = Res.drawable.search_icon,
                        contentDescription = stringResource(HomeRes.string.home_action_search),
                        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = { onNavigate(HomeNavigationEvent.Search) },
                    ),
                    AppBarAction(
                        icon = Res.drawable.settings_icon,
                        contentDescription = stringResource(HomeRes.string.home_action_settings),
                        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = { onNavigate(HomeNavigationEvent.Settings) },
                    ),
                ),
            modifier = Modifier.statusBarsPadding(),
        )
        when (state) {
            HomeUiState.Loading -> HomeLoading(Modifier.weight(1f))
            is HomeUiState.Error ->
                HomeError(
                    message = state.message,
                    onRetry = { onIntent(HomeIntent.Retry) },
                    modifier = Modifier.weight(1f),
                )

            HomeUiState.Empty ->
                HomeEmpty(
                    onCreateCard = { onNavigate(HomeNavigationEvent.CreateCard) },
                    onImportDeck = { onNavigate(HomeNavigationEvent.ImportDeck) },
                    modifier = Modifier.weight(1f),
                )

            is HomeUiState.Content ->
                HomeContent(
                    content = state,
                    onReview = { onNavigate(HomeNavigationEvent.Review) },
                    onViewStatistics = { onNavigate(HomeNavigationEvent.Statistics) },
                    onAllDecks = { onNavigate(HomeNavigationEvent.AllDecks) },
                    onOpenDeck = { onNavigate(HomeNavigationEvent.OpenDeck(it)) },
                    modifier = Modifier.weight(1f),
                )
        }
    }
}
