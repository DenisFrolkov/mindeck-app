package com.mindeck.presentation.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.dialog.WriteModalWindow
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenu
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenuItem
import com.mindeck.presentation.ui.components.item.DisplayItem
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.navigation.CardRoute
import com.mindeck.presentation.ui.navigation.CreationCardRoute
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.DeckNavigationEvent
import com.mindeck.presentation.viewmodel.DeckScreenData
import com.mindeck.presentation.viewmodel.DeckViewModel

@Composable
fun DeckScreen(
    navigator: Navigator,
    deckId: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<DeckViewModel>()

    val screenUiState by viewModel.screenUiState.collectAsStateWithLifecycle()
    val renameDeckState by viewModel.renameDeckState.collectAsStateWithLifecycle()
    val modalState by viewModel.modalState.collectAsStateWithLifecycle()

    LaunchedEffect(deckId) {
        viewModel.loadDeckWithCards(deckId)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                DeckNavigationEvent.GoBack -> navigator.pop()
                is DeckNavigationEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        context.getString(event.messageRes),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    DeckScreenContent(
        screenUiState = screenUiState,
        renameDeckState = renameDeckState,
        modalState = modalState,
        actions = DeckScreenActions(
            onMenuClick = viewModel::showDropdownMenu,
            onDismissModal = viewModel::hideModal,
            onShowRenameDialog = viewModel::showRenameDialog,
            onDeleteDeck = viewModel::deleteDeck,
            onRenameDeck = { id, name -> viewModel.renameDeck(id, name) },
            onNavigateBack = navigator::pop,
            onNavigateToCard = { navigator.push(CardRoute(it)) },
            onNavigateToCreateCard = { navigator.push(CreationCardRoute(it)) },
        ),
        modifier = modifier,
    )
}

@Composable
internal fun DeckScreenContent(
    screenUiState: UiState<DeckScreenData>,
    renameDeckState: UiState<Unit>,
    modalState: ModalState,
    actions: DeckScreenActions,
    modifier: Modifier = Modifier,
) {
    val dataSuccess = (screenUiState as? UiState.Success<DeckScreenData>)?.data

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            AppTopBar(
                showMenuButton = screenUiState is UiState.Success,
                onBackClick = actions.onNavigateBack,
                onMenuClick = actions.onMenuClick,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = dimensionResource(R.dimen.dimen_16)),
            ) {
                DeckContent(
                    screenUiState = screenUiState,
                    onNavigateToCard = actions.onNavigateToCard,
                )
            }

            dataSuccess?.let { data ->
                AppDropdownMenu(
                    padding = padding,
                    isExpanded = modalState is ModalState.DropdownMenu,
                    onDismiss = actions.onDismissModal,
                ) {
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_rename_list),
                        onClick = {
                            actions.onDismissModal()
                            actions.onShowRenameDialog()
                        },
                    )
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_create_card),
                        onClick = {
                            actions.onDismissModal()
                            actions.onNavigateToCreateCard(data.deck.deckId)
                        },
                    )
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_remove_deck),
                        onClick = {
                            actions.onDismissModal()
                            actions.onDeleteDeck(data.deck.deckId)
                        },
                    )
                }
            }
        },
    )

    when (modalState) {
        is ModalState.RenameDialog -> {
            WriteModalWindow(
                titleText = stringResource(R.string.rename_title_item_dialog),
                buttonText = stringResource(R.string.save_text),
                placeholder = stringResource(R.string.rename_item_dialog_text_input_title_deck),
                actionState = renameDeckState,
                onExitClick = actions.onDismissModal,
                onSaveClick = { name ->
                    dataSuccess?.let { data ->
                        actions.onRenameDeck(data.deck.deckId, name)
                    }
                },
            )
        }

        else -> Unit
    }
}

@Composable
private fun DeckContent(
    screenUiState: UiState<DeckScreenData>,
    onNavigateToCard: (Int) -> Unit,
) {
    when (screenUiState) {
        is UiState.Success -> {
            val screenData = screenUiState.data
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_6)),
            ) {
                if (screenData.cards.isNotEmpty()) {
                    item {
                        Text(
                            text = screenData.deck.deckName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_8)))
                    }
                }

                if (screenData.cards.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.empty_cards_list),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(R.dimen.dimen_40)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                items(
                    items = screenData.cards,
                    key = { it.cardId },
                ) { card ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        icon = R.drawable.card_icon,
                        name = card.cardName,
                        onClick = { onNavigateToCard(card.cardId) },
                    )
                }
            }
        }

        UiState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_14)))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimensionResource(R.dimen.dimen_2),
                )
            }
        }

        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_16)),
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_40)))
                Text(
                    stringResource(R.string.error_get_cards_by_deck_id),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                )
                Icon(
                    painter = painterResource(R.drawable.img_error),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(dimensionResource(R.dimen.dimen_36)),
                )
            }
        }

        UiState.Idle -> Unit
    }
}

data class DeckScreenActions(
    val onMenuClick: () -> Unit,
    val onDismissModal: () -> Unit,
    val onShowRenameDialog: () -> Unit,
    val onDeleteDeck: (Int) -> Unit,
    val onRenameDeck: (Int, String) -> Unit,
    val onNavigateBack: () -> Unit,
    val onNavigateToCard: (Int) -> Unit,
    val onNavigateToCreateCard: (Int) -> Unit,
)

@Preview(showSystemUi = true)
@Composable
private fun DeckScreenContentPreview() {
    MindeckTheme {
        DeckScreenContent(
            screenUiState = UiState.Success(
                DeckScreenData(
                    deck = previewDeck,
                    cards = listOf(previewCard, previewCard.copy(cardId = 2, cardName = "StateFlow")),
                ),
            ),
            renameDeckState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DeckScreenContentLoadingPreview() {
    MindeckTheme {
        DeckScreenContent(
            screenUiState = UiState.Loading,
            renameDeckState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DeckScreenContentErrorPreview() {
    MindeckTheme {
        DeckScreenContent(
            screenUiState = UiState.Error(R.string.error_get_cards_by_deck_id),
            renameDeckState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

private val previewActions = DeckScreenActions(
    onMenuClick = {},
    onDismissModal = {},
    onShowRenameDialog = {},
    onDeleteDeck = {},
    onRenameDeck = { _, _ -> },
    onNavigateBack = {},
    onNavigateToCard = {},
    onNavigateToCreateCard = {},
)

private val previewDeck = Deck(deckId = 1, deckName = "Английский язык")

private val previewCard = Card(
    cardName = "Kotlin корутины",
    cardQuestion = "Что такое coroutine?",
    cardAnswer = "Лёгковесная сопрограмма для асинхронного кода",
    cardType = CardType.SIMPLE,
    cardTag = "",
    deckId = 1,
)
