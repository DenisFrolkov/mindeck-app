package com.mindeck.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.dialog.data_class.CreateItemDialog
import com.mindeck.presentation.ui.components.dialog.DialogState
import com.mindeck.presentation.ui.components.dialog.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.UiState.Loading.mapSuccess
import com.mindeck.presentation.state.UiState.Loading.mapToUiState
import com.mindeck.presentation.ui.components.common.ButtonMoveMode
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.DeleteItemDialog
import com.mindeck.presentation.ui.components.dialog.data_class.DialogType
import com.mindeck.presentation.ui.components.dialog.SelectItemDialog
import com.mindeck.presentation.ui.components.dialog.animateToastItem
import com.mindeck.presentation.viewmodel.DeckViewModel
import kotlinx.coroutines.delay

@Composable
fun DeckScreen(
    navController: NavController,
    deckId: Int
) {
    val deckViewModel: DeckViewModel = hiltViewModel(navController.currentBackStackEntry!!)

    LaunchedEffect(deckId) {
        deckViewModel.getDeckById(deckId)
        deckViewModel.loadCardsForDeck(deckId)
    }


    val deck by deckViewModel.deckUIState.collectAsState()
    val cards by deckViewModel.listCardsUiState.collectAsState()
    val decks by deckViewModel.listDecksUiState.collectAsState()
    val isEditModeEnabled by deckViewModel.isEditModeEnabled.collectAsState()
    val selectedCards by deckViewModel.selectedCardIdSet.collectAsState()

    val dropdownMenuState = remember { DropdownMenuState() }
    val dialogState = remember { DialogState() }

    val selectedElement by dialogState.isSelectItem.collectAsState()
    val validation = dialogState.dialogStateData.isValid
    val toastMessage = dialogState.toastTextEvent
    val toastValue = dialogState.toastBooleanEvent

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )
    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dialogState.animateExpandedAlpha,
        animationDuration = dialogState.animationDuration * 3
    )

    val toastAlphaAnimation = animateToastItem(
        targetAlpha = dialogState.toastAlpha,
        animationDuration = dialogState.animationDuration * 5
    )

    if (toastMessage.isNotBlank()) {
        LaunchedEffect(toastValue) {
            delay(2000)
            dialogState.clearToast()
        }
    }

    var listDropdownMenu =
        dropdownMenuDataList(
            navController = navController,
            deck = deck,
            cards = cards,
            deckViewModel = deckViewModel,
            dialogState = dialogState,
            dropdownMenuState = dropdownMenuState
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
                        DeckEditTopBar(
                            selectedCards = selectedCards,
                            dialogState = dialogState,
                            deckViewModel = deckViewModel
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
                    selectedCards = selectedCards,
                    deck = deck,
                    cards = cards,
                    dropdownVisibleAnimation = dropdownVisibleAnimation,
                    listDropdownMenu = listDropdownMenu,
                    dropdownMenuState = dropdownMenuState,
                    deckViewModel = deckViewModel,
                )
            }
        )

        if (dialogState.isDialogVisible) {
            DeckDialog(
                navController = navController,
                deckViewModel = deckViewModel,
                dialogVisibleAnimation = dialogVisibleAnimation,
                deck = deck,
                decks = decks,
                selectedElement = selectedElement,
                validation = validation,
                dialogState = dialogState,
            )
        }

        AnimatedVisibility(
            visible = toastValue && toastMessage.isNotBlank(),
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(bottom = dimenDpResource(R.dimen.toast_padding_bottom))
            ) {
                Text(
                    text = toastMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .alpha(toastAlphaAnimation)
                        .clip(RoundedCornerShape(dimenDpResource(R.dimen.toast_corner_shape)))
                        .background(
                            MaterialTheme.colorScheme.onError.copy(
                                alpha = dimenFloatResource(
                                    R.dimen.float_zero_dot_five_significance
                                )
                            )
                        )
                        .padding(dimenDpResource(R.dimen.padding_small))
                )
            }
        }
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
                onClickButton = { deckViewModel.clearCardSelection() }
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
    navController: NavController,
    padding: PaddingValues,
    deck: UiState<Deck>,
    isEditModeEnabled: Boolean,
    selectedCards: Set<Int>,
    cards: UiState<List<Card>>,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownVisibleAnimation: Float,
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
            cards = cards,
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
            dropdownVisibleAnimation = dropdownVisibleAnimation,
            dropdownMenuState = dropdownMenuState,
        )
    }
}

@Composable
private fun dropdownMenuDataList(
    navController: NavController,
    deck: UiState<Deck>,
    cards: UiState<List<Card>>,
    deckViewModel: DeckViewModel,
    dialogState: DialogState,
    dropdownMenuState: DropdownMenuState
): List<DropdownMenuData> {
    return listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_rename_list),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                dialogState.openRenameDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_edit_cards_list),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                when (cards) {
                    is UiState.Success -> {
                        deckViewModel.toggleEditMode()
                    }
                }
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_card),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                navController.navigate(NavigationRoute.CreationCardScreen.route)
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remote_list),
            titleStyle = MaterialTheme.typography.bodyMedium,
            action = {
                dropdownMenuState.reset()
                when (cards) {
                    is UiState.Success -> {
                        if (cards.data.isNotEmpty()) {
                            dialogState.openDeleteDialog()
                        } else {
                            deck.mapSuccess { deckViewModel.deleteDeck(it) }
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

        is UiState.Loading -> {
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
        }

        is UiState.Error -> {
            Text(
                stringResource(R.string.error_get_info_about_deck),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
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
        }

        is UiState.Loading -> {
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
        }

        is UiState.Error -> {
            Text(
                stringResource(R.string.error_get_cards_by_deck_id),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
            )
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
                DeckRenameDialog(deck, dialogState, validation, deckViewModel)
            }

            (dialogState.currentDialogType == DialogType.Move || dialogState.currentDialogType == DialogType.MoveItemsAndDeleteItem) -> {
                DeckMoveDialog(
                    dialogState,
                    decks,
                    selectedElement,
                    deck,
                    deckViewModel,
                    navController
                )
            }

            dialogState.currentDialogType == DialogType.Delete -> {
                DeckDeleteDialog(deck, deckViewModel, navController, dialogState)
            }
        }
    }
}

@Composable
private fun DeckRenameDialog(
    deck: UiState<Deck>,
    dialogState: DialogState,
    validation: Boolean?,
    deckViewModel: DeckViewModel
) {
    when (deck) {
        is UiState.Success -> {
            CreateItemDialog(
                titleDialog = stringResource(R.string.rename_title_item_dialog),
                placeholder = stringResource(R.string.rename_item_dialog_text_input_title_deck),
                buttonText = stringResource(R.string.save_text),
                inputValue = dialogState.dialogStateData.text,
                isInputValid = validation == true || validation == null,
                onInputChange = { newValue ->
                    dialogState.validateFolderName(newValue)
                    dialogState.updateDialogText(newValue)
                },
                onBackClick = {
                    dialogState.closeDialog()
                },
                onSaveClick = {
                    if (dialogState.validateFolderName(dialogState.dialogStateData.text)) {
                        deckViewModel.renameDeck(
                            deckId = deck.data.deckId,
                            newDeckName = dialogState.dialogStateData.text
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

        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.outline.copy(
                            dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                        )
                    )
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one)
                )
            }
        }

        is UiState.Error -> {
            dialogState.closeDialog()
            dialogState.showErrorToast(
                stringResource(R.string.toast_message_impossible_perform_action)
            )
        }
    }
}

@Composable
private fun DeckMoveDialog(
    dialogState: DialogState,
    decks: UiState<List<Deck>>,
    selectedElement: Int?,
    deck: UiState<Deck>,
    deckViewModel: DeckViewModel,
    navController: NavController
) {
    when (deck) {
        is UiState.Success -> {
            SelectItemDialog(
                titleDialog = stringResource(R.string.dialog_select_deck),
                dialogState = dialogState,
                selectItems = decks.mapToUiState { deckPairs ->
                    deckPairs.map { Pair(it.deckName, it.deckId) }
                },
                selectedElement = selectedElement,
                sourceLocation = deck.data.deckId,
                fetchList = { deckViewModel.getAllDecksByFolderId(deck.data.folderId) },
                onClickSave = {
                    handleDeckMoveSave(
                        dialogState,
                        deckViewModel,
                        deck,
                        selectedElement,
                        navController
                    )
                },
            )
        }

        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.outline.copy(
                            dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                        )
                    )
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one)
                )
            }
        }

        is UiState.Error -> {
            dialogState.closeDialog()
            dialogState.showErrorToast(stringResource(R.string.toast_message_impossible_move_cards))
        }
    }
}

private fun handleDeckMoveSave(
    dialogState: DialogState,
    deckViewModel: DeckViewModel,
    deck: UiState.Success<Deck>,
    selectedElement: Int?,
    navController: NavController
) {
    if (dialogState.currentDialogType == DialogType.Move) {
        deckViewModel.moveCardsBetweenDecks(
            sourceDeckId = deck.data.deckId,
            targetDeckId = selectedElement!!,
            deckIds = deckViewModel.selectedCardIdSet.value.sorted()
                .toList()
        )
        dialogState.closeDialog()
        deckViewModel.toggleEditMode()
        deckViewModel.clearCardSelection()
    } else if (dialogState.currentDialogType == DialogType.MoveItemsAndDeleteItem) {
        deckViewModel.addCardsToDeck(
            targetDeckId = selectedElement!!,
            cardIds = deckViewModel.selectedCardIdSet.value.sorted()
                .toList()
        )
        deck.mapSuccess { deckViewModel.deleteDeck(it) }
        deckViewModel.clearCardSelection()
        dialogState.closeDialog()
        dialogState.stopSelectingDecksForMoveAndDelete()
        navController.popBackStack()
    }
}

@Composable
private fun DeckDeleteDialog(
    deck: UiState<Deck>,
    deckViewModel: DeckViewModel,
    navController: NavController,
    dialogState: DialogState
) {
    when (deck) {
        is UiState.Success -> {
            DeleteItemDialog(
                onClickDeleteAll = {
                    deckViewModel.deleteDeck(deck.data)
                    navController.popBackStack()
                },
                onClickDeletePartially = {
                    deckViewModel.toggleEditMode()
                    dialogState.closeDialog()
                    dialogState.startSelectingDecksForMoveAndDelete()
                }
            )
        }

        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.outline.copy(
                            dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                        )
                    )
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one)
                )
            }
        }

        is UiState.Error -> {
            dialogState.closeDialog()
            dialogState.showErrorToast(stringResource(R.string.toast_message_impossible_delete_deck))
        }
    }
}

@Composable
private fun DeckDropdownMenu(
    padding: PaddingValues,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownVisibleAnimation: Float,
    dropdownMenuState: DropdownMenuState
) {
    Box(modifier = Modifier
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
            .alpha(dropdownVisibleAnimation)
            .fillMaxWidth()
            .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
            .wrapContentSize(Alignment.TopEnd)
    )
}