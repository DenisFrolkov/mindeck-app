package com.mindeck.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.dialog.CreateItemDialog
import com.mindeck.presentation.ui.components.dialog.DialogState
import com.mindeck.presentation.ui.components.dialog.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.UiState.Loading.mapSuccess
import com.mindeck.presentation.state.mapToUiState
import com.mindeck.presentation.ui.components.common.ButtonMoveMode
import com.mindeck.presentation.ui.components.dialog.DeleteItemDialog
import com.mindeck.presentation.ui.components.dialog.DialogType
import com.mindeck.presentation.ui.components.dialog.SelectItemDialog
import com.mindeck.presentation.viewmodel.DeckViewModel

@Composable
fun DeckScreen(
    navController: NavController,
    deckViewModel: DeckViewModel,
) {
    var dropdownMenuState = remember { DropdownMenuState() }
    var dialogState = remember { DialogState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )
    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dialogState.animateExpandedAlpha,
        animationDuration = dialogState.animationDuration * 3
    )

    val deck = deckViewModel.deckUIState.collectAsState().value
    val cards = deckViewModel.listCardsUiState.collectAsState().value
    val decks = deckViewModel.listDecksUiState.collectAsState().value
    val isEditModeEnabled = deckViewModel.isEditModeEnabled.collectAsState().value

    val selectedCards by deckViewModel.listSelectedCards.collectAsState()
    val validation = dialogState.dialogData.isValid
    val selectedElement by dialogState.isSelectItem.collectAsState()

    var listDropdownMenu =
        dropdownMenuDataList(
            dialogState,
            navController,
            deckViewModel,
            dropdownMenuState,
            deck,
            cards
        )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            topBar = {
                AnimatedContent(
                    targetState = isEditModeEnabled,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(100)) togetherWith fadeOut(
                            animationSpec = tween(
                                100
                            )
                        )
                    }
                ) { editModeEnabled ->
                    if (editModeEnabled) {
                        DeckEditTopBar(selectedCards, dialogState, deckViewModel)
                    } else {
                        DeckTopBar(navController, dropdownMenuState)
                    }
                }
            },
            content = { padding ->
                Content(
                    navController = navController,
                    padding = padding,
                    deckViewModel = deckViewModel,
                    deck = deck,
                    cards = cards,
                    dropdownMenuState = dropdownMenuState,
                    listDropdownMenu = listDropdownMenu,
                    dropdownVisibleAnimation = dropdownVisibleAnimation,
                    isEditModeEnabled = isEditModeEnabled,
                    selectedCards = selectedCards
                )
            }
        )

        DeckDialog(
            navController = navController,
            deckViewModel = deckViewModel,
            dialogState = dialogState,
            deck = deck,
            decks = decks,
            selectedElement = selectedElement,
            validation = validation,
            dialogVisibleAnimation = dialogVisibleAnimation
        )
    }
}

@Composable
private fun DeckEditTopBar(
    selectedCards: Set<Int>,
    dialogState: DialogState,
    deckViewModel: DeckViewModel
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
                onClickButton = { deckViewModel.clearSelection() }
            )
            if (selectedCards.isNotEmpty()) {
                ButtonMoveMode(
                    buttonTitle = stringResource(R.string.text_move_mode_top_bar_edit_button),
                    onClickButton = {
                        if (!dialogState.isSelectingDecksForMoveAndDelete) {
                            dialogState.openMoveDialog()
                        } else {
                            dialogState.openMoveItemsAndDeleteItemDialog()
                        }
                    }
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
        ActionBar(
            onBackClick = { navController.popBackStack() },
            onMenuClick = { dropdownMenuState.toggle() },
            containerModifier = Modifier
                .fillMaxWidth(),
            iconModifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(all = dimenDpResource(R.dimen.padding_small))
                .size(dimenDpResource(R.dimen.padding_medium)),
        )
    }
}

@Composable
private fun Content(
    deckViewModel: DeckViewModel,
    padding: PaddingValues,
    deck: UiState<Deck>,
    isEditModeEnabled: Boolean,
    selectedCards: Set<Int>,
    cards: UiState<List<Card>>,
    navController: NavController,
    dropdownMenuState: DropdownMenuState,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownVisibleAnimation: Float
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
            cards = cards,
            navController = navController,
            deckViewModel = deckViewModel,
            isEditModeEnabled = isEditModeEnabled,
            selectedCards = selectedCards
        )
    }
    DeckDropdownMenu(dropdownMenuState, listDropdownMenu, padding, dropdownVisibleAnimation)
}

@Composable
private fun dropdownMenuDataList(
    dialogState: DialogState,
    navController: NavController,
    deckViewModel: DeckViewModel,
    dropdownMenuState: DropdownMenuState,
    deck: UiState<Deck>,
    cards: UiState<List<Card>>
): List<DropdownMenuData> {
    return listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_rename_list),
            action = {
                dropdownMenuState.reset()
                dialogState.openRenameDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_edit_list),
            action = {
                dropdownMenuState.reset()
                deckViewModel.updateEditMode()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_card),
            action = {
                dropdownMenuState.reset()
                navController.navigate(NavigationRoute.CreationCardScreen.route)
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remote_list),
            action = {
                dropdownMenuState.reset()
                when (cards) {
                    is UiState.Success -> {
                        if (cards.data.isNotEmpty()) {
                            dialogState.openDeleteDialog()
                        } else {
                            deck.mapSuccess { it }?.let { deckViewModel.deleteDeck(it) }
                            navController.popBackStack()
                        }
                    }
                }
            }
        )
    )
}

@Composable
private fun DeckInfo(
    deck: UiState<Deck>,
) {
    when (deck) {
        is UiState.Success -> {
            Text(
                text = deck.data.deckName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
private fun CardInfo(
    cards: UiState<List<Card>>,
    deckViewModel: DeckViewModel,
    navController: NavController,
    selectedCards: Set<Int>,
    isEditModeEnabled: Boolean
) {
    when (cards) {
        is UiState.Success -> {
            DisplayItemCount(
                plurals = R.plurals.card_amount,
                count = cards.data.size,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            LazyColumn {
                items(items = cards.data, key = { it.cardId }) { card ->
                    DisplayCardItem(
                        showCount = false,
                        showEditMode = isEditModeEnabled,
                        isSelected = selectedCards.contains(card.cardId),
                        onCheckedChange = { deckViewModel.toggleCardSelection(card.cardId) },
                        itemIcon = painterResource(R.drawable.card_icon),
                        itemName = card.cardName,
                        backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                            dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                        ),
                        iconColor = MaterialTheme.colorScheme.outlineVariant,
                        textStyle = MaterialTheme.typography.bodyMedium,
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
                            }
                    )
                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
                }
            }
        }
    }
}

@Composable
private fun DeckDialog(
    navController: NavController,
    dialogState: DialogState,
    dialogVisibleAnimation: Float,
    deck: UiState<Deck>,
    validation: Boolean?,
    deckViewModel: DeckViewModel,
    decks: UiState<List<Deck>>,
    selectedElement: Int?
) {
    if (dialogState.isDialogVisible) {
        Box(
            modifier = Modifier
                .alpha(dialogVisibleAnimation)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.outline.copy(dimenFloatResource(R.dimen.float_zero_dot_five_significance)))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
            )
            when {
                dialogState.currentDialogType == DialogType.Rename -> {
                    when (deck) {
                        is UiState.Success -> {
                            CreateItemDialog(
                                titleDialog = stringResource(R.string.rename_title_item_dialog),
                                placeholder = stringResource(R.string.rename_item_dialog_text_input_title_deck),
                                buttonText = stringResource(R.string.save_text),
                                value = dialogState.dialogData.text,
                                validation = validation == true || validation == null,
                                onValueChange = { newValue ->
                                    dialogState.validateFolderName(newValue)
                                    dialogState.updateDialogText(newValue)
                                },
                                onBackClick = {
                                    dialogState.closeDialog()
                                },
                                onClickButton = {
                                    if (dialogState.validateFolderName(dialogState.dialogData.text)) {
                                        deckViewModel.renameDeck(
                                            deckId = deck.data.deckId,
                                            newDeckName = dialogState.dialogData.text
                                        )
                                        dialogState.closeDialog()
                                        deckViewModel.getDeckById(deck.data.deckId)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.CenterStart),
                                iconModifier = Modifier
                                    .clip(shape = MaterialTheme.shapes.extraLarge)
                                    .background(
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                    .padding(dimenDpResource(R.dimen.padding_small))
                                    .size(dimenDpResource(R.dimen.padding_medium)),
                            )
                        }
                    }
                }

                (dialogState.currentDialogType == DialogType.Move || dialogState.currentDialogType == DialogType.MoveItemsAndDeleteItem) -> {
                    SelectItemDialog(
                        titleDialog = stringResource(R.string.dialog_select_folder),
                        dialogState = dialogState,
                        selectItems = decks.mapToUiState { deck ->
                            deck.map { Pair(it.deckName, it.deckId) }
                        },
                        selectedElement = selectedElement,
                        sourceLocation = deck.mapSuccess { it.deckId }!!,
                        fetchList = { deckViewModel.getAllDecksByFolderId(deck.mapSuccess { it.folderId }!!) },
                        onClickSave = {
                            if (dialogState.currentDialogType == DialogType.Move) {
                                deckViewModel.moveCardsBetweenDecks(
                                    sourceDeckId = deck.mapSuccess { it.deckId }!!,
                                    targetDeckId = selectedElement!!,
                                    deckIds = deckViewModel.listSelectedCards.value.sorted()
                                        .toList()
                                )
                                dialogState.closeDialog()
                                deckViewModel.updateEditMode()
                                deckViewModel.clearSelection()
                            } else if (dialogState.currentDialogType == DialogType.MoveItemsAndDeleteItem) {
                                deckViewModel.addCardsToDeck(
                                    targetDeckId = selectedElement!!,
                                    cardIds = deckViewModel.listSelectedCards.value.sorted()
                                        .toList()
                                )
                                deck.mapSuccess { it }
                                    ?.let { deckViewModel.deleteDeck(it) }
                                deckViewModel.clearSelection()
                                dialogState.closeDialog()
                                dialogState.stopSelectingDecksForMoveAndDelete()
                                navController.popBackStack()
                            }
                        },
                    )
                }

                dialogState.currentDialogType == DialogType.Delete -> {
                    DeleteItemDialog(
                        onClickDeleteAll = {
                            deck.mapSuccess { it }
                                ?.let { deckViewModel.deleteDeck(it) }
                            navController.popBackStack()
                        },
                        onClickDeletePartially = {
                            deckViewModel.updateEditMode()
                            dialogState.closeDialog()
                            dialogState.startSelectingDecksForMoveAndDelete()
                        }
                    )

                }
            }
        }
    }
}

@Composable
private fun DeckDropdownMenu(
    dropdownMenuState: DropdownMenuState,
    listDropdownMenu: List<DropdownMenuData>,
    padding: PaddingValues,
    dropdownVisibleAnimation: Float
) {
    if (dropdownMenuState.isExpanded) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { dropdownMenuState.toggle() })

        DropdownMenu(
            listDropdownMenuItem = listDropdownMenu,
            dropdownModifier = Modifier
                .padding(padding)
                .alpha(dropdownVisibleAnimation)
                .fillMaxWidth()
                .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
                .wrapContentSize(Alignment.TopEnd)
        )
    }
}