package com.mindeck.presentation.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.ReviewType
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderUiState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.getOrNull
import com.mindeck.presentation.state.onSuccess
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.common.ButtonMoveMode
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.ChooseElement
import com.mindeck.presentation.ui.components.dialog.CustomModalWindow
import com.mindeck.presentation.ui.components.dialog.DeleteItemModalWindow
import com.mindeck.presentation.ui.components.dialog.EditElementModalWindow
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.DeckViewModel

@Composable
fun DeckScreen(
    navController: NavController,
    deckId: Int
) {
    val context = LocalContext.current

    val deckViewModel: DeckViewModel = hiltViewModel(navController.currentBackStackEntry!!)

    LaunchedEffect(deckId) {
        deckViewModel.getDeckById(deckId)
        deckViewModel.loadCardsForDeck(deckId)
    }

    val deck by deckViewModel.deckUIState.collectAsState()
    val cards by deckViewModel.listCardsUiState.collectAsState()
    val decks by deckViewModel.listDecksUiState.collectAsState()
    val isEditModeEnabled by deckViewModel.isEditModeEnabled.collectAsState()
    val selectedCardIdSet by deckViewModel.selectedCardIdSet.collectAsState()
    val selectedDeckId by deckViewModel.selectedDeckId.collectAsState()

    val renameDeckState by deckViewModel.renameDeckState.collectAsState()
    val renameModalWindowValue by deckViewModel.renameModalWindowValue.collectAsState()

    val editCardsInDeckModalWindowValue by deckViewModel.editCardsInDeckModalWindowValue.collectAsState()
    val moveCardsBetweenDecksState by deckViewModel.moveCardsBetweenDecksState.collectAsState()
    val deleteDeckModalWindowValue by deckViewModel.deleteDeckModalWindowValue.collectAsState()
    val deleteCardsModalWindowValue by deckViewModel.deleteCardsModalWindowValue.collectAsState()

    val dropdownMenuState = remember { DropdownMenuState() }

    val sourceDeckId = deck.getOrNull()

    fun handleEditTopBarAction() {
        if (deleteCardsModalWindowValue) {
            deckViewModel.deleteCardsBetweenDeck(
                selectedCardIdSet.toList(),
                sourceDeckId?.deckId
            )
            deckViewModel.toggleDeleteCardsModalWindow(false)
        } else {
            deckViewModel.toggleEditCardsInDeckModalWindow(true)
            deckViewModel.getAllDecks()
        }
    }

    val listDropdownMenu =
        dropdownMenuDataList(
            context,
            navController,
            deck,
            cards,
            deckViewModel,
            dropdownMenuState
        )

    DeckContent(
        navController,
        deck,
        cards,
        dropdownMenuState,
        isEditModeEnabled,
        listDropdownMenu,
        selectedCardIdSet,
        deleteCardsModalWindowValue,
        editTopAppBarActionButton = {
            handleEditTopBarAction()
        },
        editTopAppBarExitButton = {
            deckViewModel.toggleEditMode()
            deckViewModel.toggleMovementButton()
            if (deleteCardsModalWindowValue) deckViewModel.toggleDeleteCardsModalWindow(false)
        },
        toggleCardSelection = { cardId ->
            deckViewModel.toggleCardSelection(cardId)
        }
    )

    if (renameModalWindowValue) {
        RenameDeckModalWindow(
            deck,
            renameDeckState,
            {
                deckViewModel.toggleRenameModalWindow(it)
            },
            { id, deckName ->
                deckViewModel.renameDeck(id, deckName)
            }
        )
    }

    if (editCardsInDeckModalWindowValue) {
        EditElementModalWindow(
            decks,
            deck,
            moveCardsBetweenDecksState,
            isEditModeEnabled,
            selectedDeckId,
            {
                deckViewModel.toggleEditCardsInDeckModalWindow(it)
            },
            editElement = {
                deckViewModel.moveCardsBetweenDecks(
                    selectedCardIdSet.toList(),
                    sourceDeckId?.deckId,
                    selectedDeckId
                )
            },
            chooseElement = {
                deckViewModel.toggleDeckSelection(it)
            }
        )
    }

    if (deleteDeckModalWindowValue) {
        DeleteModalWindow(
            deck,
            navController,
            {
                deckViewModel.toggleDeleteDeckModalWindow(false)
            },
            {
                deckViewModel.toggleDeleteDeckModalWindow(false)
                navController.navigate(NavigationRoute.MainScreen.route)
                deckViewModel.deleteDeck(it)
            }
        )
    }
}

@Composable
private fun RenameDeckModalWindow(
    deck: UiState<Deck>,
    renameDeckState: UiState<Unit>,
    toggleRenameModalWindow: (Boolean) -> Unit,
    renameDeck: (Int, String) -> Unit
) {
    deck.onSuccess { deckInfo ->
        Dialog(
            onDismissRequest = {
                toggleRenameModalWindow(false)
            }
        ) {
            CustomModalWindow(
                stringResource(R.string.rename_title_item_dialog),
                stringResource(R.string.save_text),
                stringResource(R.string.rename_item_dialog_text_input_title_deck),
                renameDeckState,
                exitButton = {
                    toggleRenameModalWindow(false)
                },
                saveButton = {
                    renameDeck(deckInfo.deckId, it)
                }
            )
        }
    }
}

@Composable
private fun EditElementModalWindow(
    decks: UiState<List<Deck>>,
    deck: UiState<Deck>,
    moveCardsBetweenDecksState: UiState<Unit>,
    isEditModeEnabled: Boolean,
    selectedDeckId: Int?,
    toggleEditCardsInDeckModalWindow: (Boolean) -> Unit,
    editElement: () -> Unit,
    chooseElement: (Int) -> Unit
) {
    val sourceDeckId = deck.getOrNull()

    decks.onSuccess { deck ->
        Dialog(
            onDismissRequest = {
                toggleEditCardsInDeckModalWindow(false)
            }
        ) {
            EditElementModalWindow(
                stringResource(R.string.rename_title_item_dialog),
                stringResource(R.string.save_text),
                deck.filter { it.deckId != sourceDeckId?.deckId }
                    .map { ChooseElement(it.deckName, it.deckId) },
                moveCardsBetweenDecksState,
                isEditModeEnabled,
                selectedDeckId,
                exitButton = {
                    toggleEditCardsInDeckModalWindow(false)
                },
                saveButton = {
                    editElement()
                },
                onEditItem = {
                    chooseElement(it)
                }
            )
        }
    }
}

@Composable
private fun DeleteModalWindow(
    deck: UiState<Deck>,
    navController: NavController,
    toggleDeleteDeckModalWindow: (Boolean) -> Unit,
    deleteItem: (Deck) -> Unit
) {
    deck.onSuccess { deck ->
        Dialog(
            onDismissRequest = {
                toggleDeleteDeckModalWindow(false)
            }
        ) {
            DeleteItemModalWindow(
                "Удалить колоду",
                "Удаляя колоду, вы удаляете и все карточки в ней!",
                {
                    toggleDeleteDeckModalWindow(false)
                    navController.navigate(NavigationRoute.MainScreen.route)
                    deleteItem(deck)
                },
                {
                    toggleDeleteDeckModalWindow(false)
                }
            )
        }
    }
}

@Composable
private fun DeckContent(
    navController: NavController,
    deck: UiState<Deck>,
    cards: UiState<List<Card>>,
    dropdownMenuState: DropdownMenuState,
    isEditModeEnabled: Boolean,
    listDropdownMenu: List<DropdownMenuData>,
    selectedCardIdsSet: Set<Int>,
    deleteCardsModalWindowValue: Boolean,
    editTopAppBarActionButton: () -> Unit,
    editTopAppBarExitButton: () -> Unit,
    toggleCardSelection: (Int) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AnimatedContent(
                targetState = isEditModeEnabled,
                transitionSpec = {
                    fadeIn(tween(50)) togetherWith fadeOut(tween(50))
                }
            ) { editMode ->
                if (editMode) {
                    DeckEditTopBar(
                        selectedCards = selectedCardIdsSet,
                        buttonTitle = if (deleteCardsModalWindowValue) stringResource(R.string.dropdown_menu_data_remove_cards) else stringResource(
                            R.string.text_move_mode_top_bar_edit_button
                        ),
                        onExitButton = {
                            editTopAppBarExitButton()
                        },
                        onActionButton = {
                            editTopAppBarActionButton()
                        }
                    )
                } else {
                    DeckTopBar(
                        navController = navController,
                        dropdownMenuState = dropdownMenuState
                    )
                }
            }
        },
        content = { padding ->
            Content(
                navController = navController,
                padding = padding,
                isEditModeEnabled = isEditModeEnabled,
                selectedCards = selectedCardIdsSet,
                deck = deck,
                cards = cards,
                listDropdownMenu = listDropdownMenu,
                dropdownMenuState = dropdownMenuState,
                toggleCardSelection = { cardId ->
                    toggleCardSelection(cardId)
                }
            )
        }
    )
}

@Composable
private fun DeckEditTopBar(
    selectedCards: Set<Int>,
    buttonTitle: String,
    onExitButton: () -> Unit,
    onActionButton: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonMoveMode(
                buttonTitle = stringResource(R.string.text_move_mode_top_bar_back_button),
                onClickButton = {
                    onExitButton()
                }
            )
            if (selectedCards.isNotEmpty()) {
                ButtonMoveMode(
                    buttonTitle = buttonTitle,
                    onClickButton = onActionButton
                )
            }
        }
    }
}

@Composable
private fun DeckTopBar(
    navController: NavController,
    dropdownMenuState: DropdownMenuState
) {
    Box(
        modifier = Modifier
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionHandlerButton(
                iconPainter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.back_screen_icon_button),
                iconTint = MaterialTheme.colorScheme.onPrimary,
                onClick = { navController.popBackStack() },
            )
            ActionHandlerButton(
                iconPainter = painterResource(R.drawable.menu_icon),
                contentDescription = stringResource(R.string.back_screen_icon_button),
                iconTint = MaterialTheme.colorScheme.onPrimary,
                onClick = { dropdownMenuState.toggle() },
            )
        }
    }
}

@Composable
private fun Content(
    navController: NavController,
    padding: PaddingValues,
    deck: UiState<Deck>,
    isEditModeEnabled: Boolean,
    selectedCards: Set<Int>,
    cards: UiState<List<Card>>,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownMenuState: DropdownMenuState,
    toggleCardSelection: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
    ) {
        CardInfo(
            deck = deck,
            cardsState = cards,
            navController = navController,
            isEditModeEnabled = isEditModeEnabled,
            selectedCards = selectedCards,
            toggleCardSelection = { cardId ->
                toggleCardSelection(cardId)
            }
        )
    }
    if (dropdownMenuState.isExpanded) {
        DeckDropdownMenu(
            padding = padding,
            listDropdownMenu = listDropdownMenu,
            dropdownMenuState = dropdownMenuState,
        )
    }
}

@Composable
private fun dropdownMenuDataList(
    context: Context,
    navController: NavController,
    deck: UiState<Deck>?,
    cardsState: UiState<List<Card>>,
    deckViewModel: DeckViewModel,
    dropdownMenuState: DropdownMenuState,
): List<DropdownMenuData> {
    val sourceDeck = deck?.getOrNull()

    return listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_rename_list),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                deckViewModel.toggleRenameModalWindow(true)
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_card),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                sourceDeck?.let {
                    navController.navigate(
                        NavigationRoute.CreationCardScreen.createRoute(
                            it.deckId
                        )
                    )
                }
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_edit_cards_list),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                cardsState.onSuccess {
                    if (it.isNotEmpty())
                        deckViewModel.toggleEditMode()
                    else
                        Toast.makeText(context, "Колода пуста!", Toast.LENGTH_LONG).show()
                }
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remove_cards),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                cardsState.onSuccess {
                    if (it.isNotEmpty()) {
                        dropdownMenuState.reset()
                        deckViewModel.toggleEditMode()
                        deckViewModel.toggleDeleteCardsModalWindow(true)
                    } else
                        Toast.makeText(context, "Колода пуста!", Toast.LENGTH_LONG).show()
                }
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remove_deck),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                deckViewModel.deleteDeckOrIncludeModalWindow(deck) {
                    navController.popBackStack()
                }
            }
        )
    )
}

@Composable
private fun DeckInfo(
    deckState: UiState<Deck>,
) {
    deckState.RenderUiState(
        onSuccess = { deck ->
            Text(
                text = deck.deckName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        },
        onLoading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimenDpResource(R.dimen.padding_large))
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimenDpResource(R.dimen.circular_progress_indicator_size)),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_two)
                )
            }
        },
        onError = {
            Text(
                stringResource(R.string.error_get_info_about_deck),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    )
}

@Composable
private fun CardInfo(
    deck: UiState<Deck>,
    cardsState: UiState<List<Card>>,
    navController: NavController,
    selectedCards: Set<Int>,
    isEditModeEnabled: Boolean,
    toggleCardSelection: (Int) -> Unit
) {
    cardsState.RenderUiState(
        onSuccess = { cards ->
            LazyColumn {
                item {
                    DeckInfo(deck)
                    Spacer(Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
                    DisplayItemCount(
                        plurals = R.plurals.card_amount,
                        count = cards.size,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
                items(items = cards, key = { it.cardId }) { card ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                dimenDpResource(R.dimen.border_width_dot_two_five),
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.extraSmall
                            )
                            .clip(shape = MaterialTheme.shapes.extraSmall)
                            .height(dimenDpResource(R.dimen.display_card_item_size))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigate(
                                    NavigationRoute.CardScreen.createRoute(
                                        card.cardId
                                    )
                                )
                            },
                        showCount = false,
                        showEditMode = isEditModeEnabled,
                        isSelected = selectedCards.contains(card.cardId),
                        onCheckedChange = {
                            toggleCardSelection(card.cardId)
                        },
                        displayItemData = DisplayItemData(
                            itemIcon = R.drawable.card_icon,
                            itemName = card.cardName,
                        ),
                        displayItemStyle = DisplayItemStyle(
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                                dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                            ),
                            iconColor = MaterialTheme.colorScheme.outlineVariant,
                            textStyle = MaterialTheme.typography.bodyMedium,
                        )
                    )
                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
                }
            }


        },
        onLoading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimenDpResource(R.dimen.padding_large))
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one)
                )
            }
        },
        onError = {
            Text(
                stringResource(R.string.error_get_cards_by_deck_id),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    )
}


@Composable
private fun DeckDropdownMenu(
    padding: PaddingValues,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownMenuState: DropdownMenuState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { dropdownMenuState.toggle() })

    DropdownMenu(
        listDropdownMenuItem = listDropdownMenu,
        dropdownModifier = Modifier
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(padding)
            .fillMaxWidth()
            .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
            .wrapContentSize(Alignment.TopEnd)
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreview() {
    val navController = rememberNavController()
    val cardsState: UiState<List<Card>> = cardsDataMock()
    val deckState: UiState<Deck> = deckDataMock()
    val isEditModeEnabled = false
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        DeckContent(
            navController = navController,
            deck = deckState,
            cards = cardsState,
            dropdownMenuState = dropdownMenuState,
            isEditModeEnabled = isEditModeEnabled,
            listDropdownMenu = emptyList<DropdownMenuData>(),
            selectedCardIdsSet = emptySet<Int>(),
            deleteCardsModalWindowValue = false,
            editTopAppBarActionButton = {},
            editTopAppBarExitButton = {},
            toggleCardSelection = {}
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreviewLandscape() {
    val navController = rememberNavController()
    val cardsState: UiState<List<Card>> = cardsDataMock()
    val deckState: UiState<Deck> = deckDataMock()
    val isEditModeEnabled = false
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        DeckContent(
            navController = navController,
            deck = deckState,
            cards = cardsState,
            dropdownMenuState = dropdownMenuState,
            isEditModeEnabled = isEditModeEnabled,
            listDropdownMenu = emptyList<DropdownMenuData>(),
            selectedCardIdsSet = emptySet<Int>(),
            deleteCardsModalWindowValue = false,
            editTopAppBarActionButton = {},
            editTopAppBarExitButton = {},
            toggleCardSelection = {}
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun RenameDeckModalWindowScreenPreview() {
    val deckState: UiState<Deck> = deckDataMock()
    val isEditModeEnabled = false
    val navController = rememberNavController()
    val cardsState: UiState<List<Card>> = cardsDataMock()
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        DeckContent(
            navController = navController,
            deck = deckState,
            cards = cardsState,
            dropdownMenuState = dropdownMenuState,
            isEditModeEnabled = isEditModeEnabled,
            listDropdownMenu = emptyList<DropdownMenuData>(),
            selectedCardIdsSet = emptySet<Int>(),
            deleteCardsModalWindowValue = false,
            editTopAppBarActionButton = {},
            editTopAppBarExitButton = {},
            toggleCardSelection = {}
        )
        RenameDeckModalWindow(
            deckState,
            UiState.Success(Unit),
            { _ -> },
            { _, _ -> }
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun EditElementModalWindowScreenPreview() {
    val decksState: UiState<List<Deck>> = decksDataMock()
    val deckState: UiState<Deck> = deckDataMock()
    val isEditModeEnabled = false
    val navController = rememberNavController()
    val cardsState: UiState<List<Card>> = cardsDataMock()
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        DeckContent(
            navController = navController,
            deck = deckState,
            cards = cardsState,
            dropdownMenuState = dropdownMenuState,
            isEditModeEnabled = isEditModeEnabled,
            listDropdownMenu = emptyList<DropdownMenuData>(),
            selectedCardIdsSet = emptySet<Int>(),
            deleteCardsModalWindowValue = false,
            editTopAppBarActionButton = {},
            editTopAppBarExitButton = {},
            toggleCardSelection = {}
        )
        EditElementModalWindow(
            decksState,
            deckState,
            UiState.Success(Unit),
            true,
            1,
            { _ -> },
            { },
            { _ -> }
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun DeleteModalWindowScreenPreview() {
    val deckState: UiState<Deck> = deckDataMock()
    val isEditModeEnabled = false
    val navController = rememberNavController()
    val cardsState: UiState<List<Card>> = cardsDataMock()
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        DeckContent(
            navController = navController,
            deck = deckState,
            cards = cardsState,
            dropdownMenuState = dropdownMenuState,
            isEditModeEnabled = isEditModeEnabled,
            listDropdownMenu = emptyList<DropdownMenuData>(),
            selectedCardIdsSet = emptySet<Int>(),
            deleteCardsModalWindowValue = false,
            editTopAppBarActionButton = {},
            editTopAppBarExitButton = {},
            toggleCardSelection = {}
        )
        DeleteModalWindow(
            deckState,
            navController,
            {},
            {}
        )
    }
}

@Composable
private fun deckDataMock(): UiState<Deck> = UiState.Success(
    Deck(
        deckId = 1,
        deckName = "Kotlin Basics"
    )
)

@Composable
private fun decksDataMock(): UiState<List<Deck>> = UiState.Success(
    listOf<Deck>(
        Deck(
            deckId = 1,
            deckName = "Kotlin Basics"
        ),
        Deck(
            deckId = 2,
            deckName = "Jetpack Compose"
        ),
        Deck(
            deckId = 3,
            deckName = "Architecture Patterns"
        ),
        Deck(
            deckId = 4,
            deckName = "Coroutines & Flow"
        )
    )
)

@Composable
private fun cardsDataMock(): UiState<List<Card>> = UiState.Success(
    listOf<Card>(
        Card(
            cardId = 1,
            cardName = "Basic Kotlin",
            cardQuestion = "Что такое data class в Kotlin?",
            cardAnswer = "Это класс, предназначенный для хранения данных. Он автоматически генерирует equals, hashCode и toString.",
            cardType = "Теория",
            cardTag = "Kotlin",
            deckId = 101,
            firstReviewDate = 1_725_000_000_000,
            lastReviewDate = 1_725_086_400_000,
            nextReviewDate = 1_725_172_800_000,
            repetitionCount = 2,
            lastReviewType = ReviewType.EASY
        ),
        Card(
            cardId = 2,
            cardName = "Android Lifecycle",
            cardQuestion = "Какой метод вызывается первым в жизненном цикле Activity?",
            cardAnswer = "onCreate()",
            cardType = "Вопрос",
            cardTag = "Android",
            deckId = 102,
            repetitionCount = 0,
            lastReviewType = null
        ),
        Card(
            cardId = 3,
            cardName = "ViewModel Purpose",
            cardQuestion = "Зачем использовать ViewModel в Android?",
            cardAnswer = "Для хранения и управления UI-данными с учетом жизненного цикла.",
            cardType = "Объяснение",
            cardTag = "Architecture",
            deckId = 103,
            firstReviewDate = 1_724_000_000_000,
            lastReviewDate = 1_725_000_000_000,
            nextReviewDate = 1_726_000_000_000,
            repetitionCount = 5,
            lastReviewType = ReviewType.HARD
        ),
        Card(
            cardId = 4,
            cardName = "Coroutine Basics",
            cardQuestion = "Что делает launch в корутинах?",
            cardAnswer = "Запускает новую корутину без возвращения результата.",
            cardType = "Программирование",
            cardTag = "Coroutines",
            deckId = 101,
            firstReviewDate = 1_725_000_000_000,
            repetitionCount = 1,
            lastReviewType = ReviewType.MEDIUM
        ),
        Card(
            cardId = 5,
            cardName = "Room Annotation",
            cardQuestion = "Какая аннотация используется для DAO интерфейса в Room?",
            cardAnswer = "@Dao",
            cardType = "Вспомни",
            cardTag = "Room",
            deckId = 102,
            repetitionCount = 3,
            lastReviewType = ReviewType.EASY
        )
    )
)