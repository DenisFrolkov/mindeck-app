package com.mindeck.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.CustomModalWindow
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.topBar.TopAppBar
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.CardRoute
import com.mindeck.presentation.ui.navigation.CreationCardRoute
import com.mindeck.presentation.ui.navigation.MainRoute
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.viewmodel.DeckEvent
import com.mindeck.presentation.viewmodel.DeckEvent.OnLoadDeckWithCards
import com.mindeck.presentation.viewmodel.DeckScreenData
import com.mindeck.presentation.viewmodel.DeckViewModel
import com.mindeck.presentation.viewmodel.NavigationEvent

@Composable
fun DeckScreen(
    navigator: Navigator,
    deckId: Int,
    modifier: Modifier = Modifier,
) {
    DeckScreenContent(
        navigator = navigator,
        deckId = deckId,
        viewModel = hiltViewModel<DeckViewModel>(),
        modifier = modifier,
    )
}

@Composable
internal fun DeckScreenContent(
    navigator: Navigator,
    deckId: Int,
    viewModel: DeckViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val screenUiState by viewModel.screenUiState.collectAsState()
    val renameDeckState by viewModel.renameDeckState.collectAsState()

    var renameModalWindow by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val dataSuccess = (screenUiState as? UiState.Success<DeckScreenData>)?.data

    LaunchedEffect(deckId) {
        viewModel.onEvent(OnLoadDeckWithCards(deckId))
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.GoToMain -> {
                    navigator.push(MainRoute)
                }

                NavigationEvent.CloseRenameWindow -> {
                    renameModalWindow = false
                }

                is NavigationEvent.ShowToast -> {
                    Toast
                        .makeText(
                            context,
                            event.message,
                            Toast.LENGTH_SHORT,
                        ).show()
                }
            }
        }
    }

    Scaffold(
        modifier =
            modifier
                .fillMaxSize(),
        topBar = {
            TopAppBar(
                visibleMenuButton = screenUiState is UiState.Success,
                onBackClick = { navigator.pop() },
                onMenuClick = { isDropdownExpanded = true },
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            DeckCardList(
                screenUiState = screenUiState,
                navigator = navigator,
                modifier =
                    Modifier
                        .padding(padding)
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )

            dataSuccess?.let { data ->
                DeckDropdownMenu(
                    padding = padding,
                    isExpanded = isDropdownExpanded,
                    onDismiss = { isDropdownExpanded = false },
                    onCreateClick = {
                        isDropdownExpanded = false
                        navigator.push(CreationCardRoute(data.deck.deckId))
                    },
                    onRenameClick = {
                        isDropdownExpanded = false
                        renameModalWindow = true
                    },
                    onDeleteClick = {
                        isDropdownExpanded = false
                        viewModel.onEvent(DeckEvent.OnDeleteDeck(data.deck))
                    },
                )
            }
        },
    )

    if (renameModalWindow) {
        CustomModalWindow(
            stringResource(R.string.rename_title_item_dialog),
            stringResource(R.string.save_text),
            stringResource(R.string.rename_item_dialog_text_input_title_deck),
            isLoading = renameDeckState is UiState.Loading,
            errorMsg = (renameDeckState as? UiState.Error)?.error,
            onExitClick = {
                renameModalWindow = false
                viewModel.onEvent(DeckEvent.OnResetRenameDeckState)
            },
            onSaveClick = {
                dataSuccess?.let { data ->
                    viewModel.onEvent(DeckEvent.OnRenameDeck(data.deck.deckId, it))
                }
            },
        )
    }
}

@Composable
private fun DeckCardList(
    screenUiState: UiState<DeckScreenData>,
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    when (screenUiState) {
        is UiState.Error -> {
            DeckErrorState(
                modifier = modifier,
            )
        }

        is UiState.Success -> {
            DeckSuccessState(
                data = screenUiState.data,
                navigator = navigator,
                modifier = modifier,
            )
        }

        else -> {
            DeckLoadingState(
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun DeckSuccessState(
    data: DeckScreenData,
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val cardItemStyle =
        DisplayItemStyle(
            backgroundColor =
                MaterialTheme.colorScheme.secondary.copy(
                    dimenFloatResource(R.dimen.float_zero_dot_five_significance),
                ),
            iconColor = MaterialTheme.colorScheme.outlineVariant,
            textStyle = MaterialTheme.typography.bodyMedium,
        )

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_6)),
    ) {
        item {
            Text(
                text = data.deck.deckName,
                style = MaterialTheme.typography.titleMedium,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(dimensionResource(R.dimen.dimen_6)),
            )
        }
        items(items = data.cards, key = { it.cardId }) { card ->
            DisplayItem(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall,
                showCount = false,
                displayItemData =
                    DisplayItemData(
                        itemIcon = R.drawable.card_icon,
                        itemName = card.cardName,
                    ),
                displayItemStyle = cardItemStyle,
                onClick = {
                    navigator.push(CardRoute(card.cardId))
                },
            )
        }
    }
}

@Composable
private fun DeckLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.padding_large)),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = dimensionResource(R.dimen.circular_progress_indicator_weight_one),
        )
    }
}

@Composable
private fun DeckErrorState(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.error_get_cards_by_deck_id),
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style =
            MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.error,
            ),
    )
}

@Composable
private fun DeckDropdownMenu(
    padding: PaddingValues,
    isExpanded: Boolean,
    onDismiss: () -> Unit,
    onRenameClick: () -> Unit,
    onCreateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .padding(horizontal = dimensionResource(R.dimen.dimen_28))
                .padding(padding)
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd),
    ) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            shape = MaterialTheme.shapes.medium,
        ) {
            DropdownMenuItem(
                text = stringResource(R.string.dropdown_menu_data_rename_list),
                onClick = onRenameClick,
            )
            DropdownMenuItem(
                text = stringResource(R.string.dropdown_menu_data_create_card),
                onClick = onCreateClick,
            )
            DropdownMenuItem(
                text = stringResource(R.string.dropdown_menu_data_remove_deck),
                onClick = onDeleteClick,
            )
        }
    }
}

@Composable
private fun DropdownMenuItem(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    horizontal = dimensionResource(R.dimen.dimen_20),
                    vertical = dimensionResource(R.dimen.dimen_10),
                ),
    )
}
