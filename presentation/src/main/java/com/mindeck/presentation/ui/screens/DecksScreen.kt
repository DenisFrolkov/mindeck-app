package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
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
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.DeckRoute
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.viewmodel.DecksViewModel

@Composable
fun DecksScreen(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    DecksScreenContent(
        navigator = navigator,
        viewModel = hiltViewModel<DecksViewModel>(),
        modifier = modifier,
    )
}

@Composable
internal fun DecksScreenContent(
    navigator: Navigator,
    viewModel: DecksViewModel,
    modifier: Modifier = Modifier,
) {
    val decks by viewModel.decksState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                onBackClick = { navigator.pop() },
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dimen_16)),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = dimensionResource(R.dimen.dimen_16))
                    .navigationBarsPadding(),
            ) {
                DecksInfo(decks) { deckId -> navigator.push(DeckRoute(deckId)) }
            }
        },
    )
}

@Composable
private fun DecksInfo(
    decksState: UiState<List<Deck>>,
    onDeckClick: (Int) -> Unit,
) {
    decksState.RenderState(
        onSuccess = { decks ->
            LazyColumn {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center),
                    ) {
                        Text(
                            text = pluralStringResource(
                                R.plurals.deck_amount,
                                decks.size,
                                decks.size,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_20)))
                }
                items(
                    items = decks,
                    key = { it.deckId },
                ) { deck ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        showCount = false,
                        displayItemData = DisplayItemData(
                            itemIcon = R.drawable.deck_icon,
                            itemName = deck.deckName,
                        ),
                        displayItemStyle = DisplayItemStyle(
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                                dimenFloatResource(R.dimen.float_zero_dot_five_significance),
                            ),
                            iconColor = MaterialTheme.colorScheme.outlineVariant,
                            textStyle = MaterialTheme.typography.bodyMedium,
                        ),
                        onClick = { onDeckClick(deck.deckId) },
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_6)))
                }
            }
        },
        onLoading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.dimen_24))
                    .wrapContentSize(Alignment.Center),
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimensionResource(R.dimen.dimen_4),
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
