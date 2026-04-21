package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.SessionSummary
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.CustomButton
import com.mindeck.presentation.ui.components.item.DisplayItem
import com.mindeck.presentation.ui.navigation.CardStudyRoute
import com.mindeck.presentation.ui.navigation.CreationCardRoute
import com.mindeck.presentation.ui.navigation.DeckRoute
import com.mindeck.presentation.ui.navigation.DecksRoute
import com.mindeck.presentation.ui.navigation.LocalNavigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current

    val viewModel = hiltViewModel<MainViewModel>()
    val decksState by viewModel.decksState.collectAsStateWithLifecycle()
    val sessionSummaryState by viewModel.sessionSummaryState.collectAsStateWithLifecycle()

    MainScreenContent(
        decksState = decksState,
        sessionSummaryState = sessionSummaryState,
        actions = MainScreenActions(
            onNavigateToStudy = { navigator.push(CardStudyRoute()) },
            onNavigateToDeck = { navigator.push(DeckRoute(it)) },
            onNavigateToDecks = { navigator.push(DecksRoute) },
            onNavigateToCreateCard = { navigator.push(CreationCardRoute()) },
        ),
        modifier = modifier,
    )
}

@Composable
internal fun MainScreenContent(
    decksState: UiState<List<Deck>>,
    sessionSummaryState: UiState<SessionSummary>,
    actions: MainScreenActions,
    modifier: Modifier = Modifier,
) {
    val hasCardsToStudy = sessionSummaryState is UiState.Success &&
        sessionSummaryState.data.totalCount > 0

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = MindeckTheme.dimensions.paddingMd),
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(MindeckTheme.dimensions.paddingSm))
                SessionSummaryRow(
                    sessionSummaryState = sessionSummaryState,
                    enabled = hasCardsToStudy,
                    onClick = actions.onNavigateToStudy,
                )
                Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerLg))
                DeckList(
                    decksState = decksState,
                    onDeckClick = actions.onNavigateToDeck,
                    onAllDecksClick = actions.onNavigateToDecks,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = MindeckTheme.dimensions.dp0,
                ),
                shape = MindeckTheme.shapes.shapeXl,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = actions.onNavigateToCreateCard,
            ) {
                Icon(
                    painter = painterResource(R.drawable.img_add),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconMd),
                )
            }
        },
    )
}

@Composable
private fun SessionSummaryRow(
    sessionSummaryState: UiState<SessionSummary>,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MindeckTheme.shapes.shapeLg)
            .background(MaterialTheme.colorScheme.surface)
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = MindeckTheme.dimensions.paddingXs),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        when (sessionSummaryState) {
            is UiState.Success -> {
                val summary = sessionSummaryState.data
                RepeatCountItem(
                    count = summary.newCount.toString(),
                    label = stringResource(R.string.new_text),
                    color = MaterialTheme.colorScheme.tertiary,
                )
                RepeatCountItem(
                    count = summary.learningCount.toString(),
                    label = stringResource(R.string.learning_text),
                    color = MaterialTheme.colorScheme.error,
                )
                RepeatCountItem(
                    count = summary.reviewCount.toString(),
                    label = stringResource(R.string.to_review_text),
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            UiState.Loading, UiState.Idle -> {
                RepeatCountItem(count = "—", label = stringResource(R.string.new_text), color = MaterialTheme.colorScheme.tertiary)
                RepeatCountItem(count = "—", label = stringResource(R.string.learning_text), color = MaterialTheme.colorScheme.error)
                RepeatCountItem(count = "—", label = stringResource(R.string.to_review_text), color = MaterialTheme.colorScheme.primary)
            }

            is UiState.Error -> {
                RepeatCountItem(count = "—", label = stringResource(R.string.new_text), color = MaterialTheme.colorScheme.tertiary)
                RepeatCountItem(count = "—", label = stringResource(R.string.learning_text), color = MaterialTheme.colorScheme.error)
                RepeatCountItem(count = "—", label = stringResource(R.string.to_review_text), color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun RepeatCountItem(
    count: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.dp1),
        modifier = modifier,
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun DeckList(
    decksState: UiState<List<Deck>>,
    onDeckClick: (Int) -> Unit,
    onAllDecksClick: () -> Unit,
) {
    when (decksState) {
        is UiState.Success -> {
            val decks = decksState.data
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacerMd),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (decks.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.empty_decks_list),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                items(
                    items = decks.take(MAX_DECK_COUNT),
                    key = { it.deckId },
                ) { deck ->
                    DisplayItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = R.drawable.deck_icon,
                        name = deck.deckName,
                        onClick = { onDeckClick(deck.deckId) },
                    )
                }

                if (decks.size > MAX_DECK_COUNT) {
                    item {
                        CustomButton(
                            text = stringResource(R.string.title_text_all_decks),
                            onClick = onAllDecksClick,
                            modifier = Modifier.size(
                                height = MindeckTheme.dimensions.dp42,
                                width = MindeckTheme.dimensions.dp140,
                            ),
                        )
                    }
                }
            }
        }

        UiState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
            ) {
                Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = MindeckTheme.dimensions.dp2,
                )
            }
        }

        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.dp16),
            ) {
                Text(
                    text = stringResource(R.string.error_get_all_decks),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
                Icon(
                    painter = painterResource(R.drawable.img_error),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconLg),
                )
            }
        }

        UiState.Idle -> Unit
    }
}

const val MAX_DECK_COUNT = 5

data class MainScreenActions(
    val onNavigateToStudy: () -> Unit,
    val onNavigateToDeck: (Int) -> Unit,
    val onNavigateToDecks: () -> Unit,
    val onNavigateToCreateCard: () -> Unit,
)
