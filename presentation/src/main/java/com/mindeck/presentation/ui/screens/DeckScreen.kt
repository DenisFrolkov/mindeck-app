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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
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

    PageContent(
        context,
        navController,
        deck,
        cards,
        deckViewModel,
        dropdownMenuState,
        isEditModeEnabled,
        selectedCardIdSet,
        selectedDeckId,
        decks,
        renameDeckState,
        renameModalWindowValue,
        editCardsInDeckModalWindowValue,
        moveCardsBetweenDecksState,
        deleteDeckModalWindowValue,
        deleteCardsModalWindowValue,
        onSaveDeck = { id, deckName ->
            deckViewModel.renameDeck(id, deckName)
        },
        toggleRenameModalWindow = {
            deckViewModel.toggleRenameModalWindow(it)
        },
        toggleEditCardsInDeckModalWindow = {
            deckViewModel.toggleEditCardsInDeckModalWindow(it)
        },
        toggleDeleteDeckModalWindow = {
            deckViewModel.toggleDeleteDeckModalWindow(it)
        },
        toggleDeleteCardsModalWindow = {
            deckViewModel.toggleDeleteCardsModalWindow(it)
        }
    )
}

@Composable
private fun PageContent(
    context: Context,
    navController: NavController,
    deck: UiState<Deck>,
    cards: UiState<List<Card>>,
    deckViewModel: DeckViewModel,
    dropdownMenuState: DropdownMenuState,
    isEditModeEnabled: Boolean,
    selectedCardIdsSet: Set<Int>,
    selectedDeckId: Int?,
    decks: UiState<List<Deck>>,
    renameDeckState: UiState<Unit>,
    renameModalWindowValue: Boolean,
    editCardsInDeckModalWindowValue: Boolean,
    moveCardsBetweenDecksState: UiState<Unit>,
    deleteDeckModalWindowValue: Boolean,
    deleteCardsModalWindowValue: Boolean,
    onSaveDeck: (Int, String) -> Unit,
    toggleRenameModalWindow: (Boolean) -> Unit,
    toggleEditCardsInDeckModalWindow: (Boolean) -> Unit,
    toggleDeleteDeckModalWindow: (Boolean) -> Unit,
    toggleDeleteCardsModalWindow: (Boolean) -> Unit
) {
    val listDropdownMenu =
        dropdownMenuDataList(
            context,
            navController,
            deck,
            cards,
            deckViewModel,
            dropdownMenuState
        )

    val sourceDeckId = deck.getOrNull()

    fun handleEditTopBarAction() {
        if (deleteCardsModalWindowValue) {
            sourceDeckId?.deckId?.let { sourceId ->
                deckViewModel.deleteCardsBetweenDeck(
                    selectedCardIdsSet.toList(),
                    sourceDeckId = sourceId
                )
            }
            toggleDeleteCardsModalWindow(false)
        } else {
            deckViewModel.toggleEditCardsInDeckModalWindow(true)
            deckViewModel.getAllDecks()
        }
    }

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
                        deleteCardsModalWindowValue = deleteCardsModalWindowValue,
                        onExitButton = {
                            deckViewModel.toggleEditMode()
                            deckViewModel.toggleMovementButton()
                            if (deleteCardsModalWindowValue) toggleDeleteCardsModalWindow(false)
                        },
                        onActionButton = {
                            handleEditTopBarAction()
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
            PageContent(
                navController = navController,
                padding = padding,
                isEditModeEnabled = isEditModeEnabled,
                selectedCards = selectedCardIdsSet,
                deck = deck,
                cards = cards,
                listDropdownMenu = listDropdownMenu,
                dropdownMenuState = dropdownMenuState,
                deckViewModel = deckViewModel,
            )

            if (renameModalWindowValue) {
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
                                onSaveDeck(deckInfo.deckId, it)
                            }
                        )
                    }
                }
            }

            if (editCardsInDeckModalWindowValue) {
                Dialog(
                    onDismissRequest = {
                        toggleEditCardsInDeckModalWindow(false)
                    }
                ) {
                    decks.onSuccess { deck ->
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
                                if (selectedDeckId != null)
                                    sourceDeckId?.deckId?.let { it ->
                                        deckViewModel.moveCardsBetweenDecks(
                                            selectedCardIdsSet.toList(),
                                            it,
                                            selectedDeckId
                                        )
                                    }
                            },
                            onEditItem = {
                                deckViewModel.toggleDeckSelection(it)
                            }
                        )
                    }
                }
            }

            if (deleteDeckModalWindowValue) {
                Dialog(
                    onDismissRequest = {
                        toggleDeleteDeckModalWindow(false)
                    }
                ) {
                    deck.onSuccess { deck ->
                        DeleteItemModalWindow(
                            "Удалить колоду",
                            "Удаляя колоду, вы удаляете и все карточки в ней!",
                            {
                                toggleDeleteDeckModalWindow(false)
                                navController.navigate(NavigationRoute.MainScreen.route)
                                deckViewModel.deleteDeck(deck)
                            },
                            {
                                toggleDeleteDeckModalWindow(false)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun DeckEditTopBar(
    selectedCards: Set<Int>,
    deleteCardsModalWindowValue: Boolean,
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
                    buttonTitle = if (deleteCardsModalWindowValue) stringResource(R.string.dropdown_menu_data_remove_cards) else stringResource(
                        R.string.text_move_mode_top_bar_edit_button
                    ),
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
private fun PageContent(
    navController: NavController,
    padding: PaddingValues,
    deck: UiState<Deck>,
    isEditModeEnabled: Boolean,
    selectedCards: Set<Int>,
    cards: UiState<List<Card>>,
    listDropdownMenu: List<DropdownMenuData>,
    deckViewModel: DeckViewModel,
    dropdownMenuState: DropdownMenuState
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
    ) {
        DeckInfo(deck)
        Spacer(Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
        CardInfo(
            cardsState = cards,
            navController = navController,
            deckViewModel = deckViewModel,
            isEditModeEnabled = isEditModeEnabled,
            selectedCards = selectedCards
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
    cardsState: UiState<List<Card>>,
    deckViewModel: DeckViewModel,
    navController: NavController,
    selectedCards: Set<Int>,
    isEditModeEnabled: Boolean
) {
    cardsState.RenderUiState(
        onSuccess = { cards ->
            DisplayItemCount(
                plurals = R.plurals.card_amount,
                count = cards.size,
                textStyle = MaterialTheme.typography.bodyMedium
            )
            LazyColumn {
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
                        onCheckedChange = { deckViewModel.toggleCardSelection(card.cardId) },
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