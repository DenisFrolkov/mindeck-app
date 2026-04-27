package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.item.DisplayItem
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.navigation.DeckRoute
import com.mindeck.presentation.ui.navigation.LocalNavigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.DecksViewModel

@Composable
fun DecksScreen(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current

    val viewModel = hiltViewModel<DecksViewModel>()
    val decksState by viewModel.decksState.collectAsStateWithLifecycle()

    DecksScreenContent(
        decksState = decksState,
        actions = DecksScreenActions(
            onNavigateBack = navigator::pop,
            onNavigateToDeck = { navigator.push(DeckRoute(it)) },
        ),
        modifier = modifier,
    )
}

@Composable
internal fun DecksScreenContent(
    decksState: UiState<List<Deck>>,
    actions: DecksScreenActions,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                onBackClick = actions.onNavigateBack,
                modifier = Modifier.padding(horizontal = MindeckTheme.dimensions.paddingMd),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = MindeckTheme.dimensions.paddingMd),
            ) {
                DecksList(
                    decksState = decksState,
                    onDeckClick = actions.onNavigateToDeck,
                )
            }
        },
    )
}

@Composable
private fun DecksList(
    decksState: UiState<List<Deck>>,
    onDeckClick: (Int) -> Unit,
) {
    when (decksState) {
        is UiState.Success -> {
            val decks = decksState.data
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacerMd),
            ) {
                if (decks.isNotEmpty()) {
                    item {
                        Text(
                            text = pluralStringResource(
                                R.plurals.deck_amount,
                                decks.size,
                                decks.size,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerSm))
                    }
                }

                if (decks.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.empty_decks_list),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = MindeckTheme.dimensions.dp40),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                items(
                    items = decks,
                    key = { it.deckId },
                ) { deck ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        icon = R.drawable.deck_icon,
                        name = deck.deckName,
                        onClick = { onDeckClick(deck.deckId) },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerSm))
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
                verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.paddingMd),
            ) {
                Spacer(modifier = Modifier.height(MindeckTheme.dimensions.dp40))
                Text(
                    stringResource(R.string.error_get_all_decks),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
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

data class DecksScreenActions(
    val onNavigateBack: () -> Unit,
    val onNavigateToDeck: (Int) -> Unit,
)
