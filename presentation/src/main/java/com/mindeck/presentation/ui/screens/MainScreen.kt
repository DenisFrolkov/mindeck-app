package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.CreationCardRoute
import com.mindeck.presentation.ui.navigation.DeckRoute
import com.mindeck.presentation.ui.navigation.DecksRoute
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.viewmodel.MainEvent.OnLoadDecks
import com.mindeck.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    MainScreenContent(
        navigator = navigator,
        viewModel = hiltViewModel<MainViewModel>(),
        modifier = modifier,
    )
}

@Composable
internal fun MainScreenContent(
    navigator: Navigator,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        viewModel.onEvent(OnLoadDecks)
//        viewModel.onEvent(MainEvent.OnLoadCardRepetition())
    }

    val decksState by viewModel.decksState.collectAsState()
    val cardsForRepetitionState by viewModel.cardsForRepetitionState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.dimen_12)))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(vertical = dimensionResource(R.dimen.dimen_8)),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    RepeatCountItem(
                        14,
                        stringResource(R.string.new_text),
                        color = Color.Green,
                        Modifier,
                    )

                    RepeatCountItem(
                        6,
                        stringResource(R.string.learning_text),
                        color = Color.Red,
                        Modifier,
                    )

                    RepeatCountItem(
                        10,
                        stringResource(R.string.to_review_text),
                        color = Color.Blue,
                        Modifier,
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
                ListDecks(
                    navigator = navigator,
                    decksState = decksState,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = dimensionResource(R.dimen.dimen_0),
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.dimen_20)),
                containerColor = MaterialTheme.colorScheme.onSecondary,
                onClick = {
                    navigator.push(CreationCardRoute())
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.dimen_24)),
                )
            }
        },
    )
}

@Composable
private fun RepeatCountItem(
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_2)),
        modifier = modifier,
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ListDecks(
    navigator: Navigator,
    decksState: UiState<List<Deck>>,
) {
    decksState.RenderState(
        onSuccess = { decks ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_8)),
            ) {
                items(
                    items = decks.take(5),
                    key = { it.deckId },
                ) { deck ->
                    DeckItem(navigator = navigator, deck = deck)
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))
                }

                if (decks.size > 5) {
                    item {
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_8)))
                        ButtonAllDecks(navigator)
                    }
                }
            }
        },
        onLoading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimensionResource(R.dimen.circular_progress_indicator_weight_one),
                )
            }
        },
        onError = {
            Text(
                stringResource(R.string.error_get_all_decks),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
            )
        },
    )
}

@Composable
private fun DeckItem(
    navigator: Navigator,
    deck: Deck,
) {
    DisplayItem(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        showCount = false,
        displayItemData = DisplayItemData(
            itemIcon = R.drawable.deck_icon,
            numberOfCards = deck.deckId,
            itemName = deck.deckName,
        ),
        displayItemStyle = DisplayItemStyle(
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                dimenFloatResource(R.dimen.float_zero_dot_five_significance),
            ),
            iconColor = MaterialTheme.colorScheme.outlineVariant,
            textStyle = MaterialTheme.typography.bodyMedium,
        ),
        onClick = {
            navigator.push(DeckRoute(deck.deckId))
        },
    )
}

@Composable
private fun ButtonAllDecks(
    navigator: Navigator,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.medium,
                )
                .clickable {
                    navigator.push(DecksRoute)
                },
        ) {
            Text(
                text = stringResource(R.string.title_text_all_decks),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.padding(
                    vertical = dimensionResource(R.dimen.padding_small),
                    horizontal = dimensionResource(R.dimen.padding_large),
                ),
            )
        }
    }
}
