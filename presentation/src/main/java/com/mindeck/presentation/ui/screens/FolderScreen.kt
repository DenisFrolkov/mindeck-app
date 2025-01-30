package com.mindeck.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.UiState.Loading.mapSuccess
import com.mindeck.presentation.state.UiState.Loading.mapToUiState
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.ButtonMoveMode
import com.mindeck.presentation.ui.components.common.DisplayItemCount
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.data_class.CreateItemDialog
import com.mindeck.presentation.ui.components.dialog.DeleteItemDialog
import com.mindeck.presentation.ui.components.dialog.DialogState
import com.mindeck.presentation.ui.components.dialog.data_class.DialogType
import com.mindeck.presentation.ui.components.dialog.SelectItemDialog
import com.mindeck.presentation.ui.components.dialog.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.viewmodel.FolderViewModel

@Composable
fun FolderScreen(
    navController: NavController,
    folderViewModel: FolderViewModel,
) {
    val dropdownMenuState = remember { DropdownMenuState() }
    val dialogState = remember { DialogState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )
    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dialogState.animateExpandedAlpha,
        animationDuration = dialogState.animationDuration * 3
    )

    val folder = folderViewModel.folderUIState.collectAsState().value
    val folders = folderViewModel.foldersUIState.collectAsState().value
    val decks = folderViewModel.deckByIdrUIState.collectAsState().value
    val isEditModeEnabled = folderViewModel.isEditModeEnabled.collectAsState().value

    val selectedDecks by folderViewModel.selectedDecks.collectAsState()
    val selectedElement by dialogState.isSelectItem.collectAsState()
    val validation = dialogState.dialogStateData.isValid

    var listDropdownMenu =
        dropdownMenuDataList(
            navController = navController,
            folder = folder,
            decks = decks,
            folderViewModel = folderViewModel,
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
                        FolderEditTopBar(
                            selectedDecks = selectedDecks,
                            folderViewModel = folderViewModel,
                            dialogState = dialogState
                        )
                    } else {
                        FolderTopBar(
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
                    selectedDecks = selectedDecks,
                    folder = folder,
                    decks = decks,
                    dropdownVisibleAnimation = dropdownVisibleAnimation,
                    listDropdownMenu = listDropdownMenu,
                    folderViewModel = folderViewModel,
                    dropdownMenuState = dropdownMenuState
                )
            }
        )

        FolderDialog(
            navController = navController,
            selectedElement = selectedElement,
            dialogVisibleAnimation = dialogVisibleAnimation,
            folder = folder,
            folders = folders,
            validation = validation,
            folderViewModel = folderViewModel,
            dialogState = dialogState
        )
    }
}

@Composable
private fun FolderEditTopBar(
    selectedDecks: Set<Int>,
    folderViewModel: FolderViewModel,
    dialogState: DialogState
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
                onClickButton = { folderViewModel.clearSelectDeck() }
            )
            if (selectedDecks.isNotEmpty()) {
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
private fun FolderTopBar(
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
    isEditModeEnabled: Boolean,
    selectedDecks: Set<Int>,
    folder: UiState<Folder>,
    decks: UiState<List<Deck>>,
    dropdownVisibleAnimation: Float,
    listDropdownMenu: List<DropdownMenuData>,
    folderViewModel: FolderViewModel,
    dropdownMenuState: DropdownMenuState
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
    ) {
        FolderInfo(folder = folder)
        Spacer(Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
        DeckInfo(
            navController = navController,
            decks = decks,
            isEditModeEnabled = isEditModeEnabled,
            selectedDecks = selectedDecks,
            folderViewModel = folderViewModel
        )
    }
    if (dropdownMenuState.isExpanded) {
        FolderDropdownMenu(
            padding = padding,
            listDropdownMenu = listDropdownMenu,
            dropdownVisibleAnimation = dropdownVisibleAnimation,
            dropdownMenuState = dropdownMenuState
        )
    }
}

@Composable
private fun dropdownMenuDataList(
    navController: NavController,
    folder: UiState<Folder>,
    decks: UiState<List<Deck>>,
    folderViewModel: FolderViewModel,
    dialogState: DialogState,
    dropdownMenuState: DropdownMenuState
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
                folderViewModel.updateEditMode()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_deck_list),
            action = {
                dropdownMenuState.reset()
                dialogState.openCreateDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remote_list),
            action = {
                dropdownMenuState.reset()
                when (decks) {
                    is UiState.Success -> {
                        if (decks.data.isNotEmpty()) {
                            dialogState.openDeleteDialog()
                        } else {
                            folder.mapSuccess { folderViewModel.deleteFolder(it) }
                            navController.popBackStack()
                        }
                    }
                }
            }
        )
    )
}


@Composable
private fun FolderInfo(
    folder: UiState<Folder>
) {
    when (folder) {
        is UiState.Success -> {
            Column {
                Text(
                    text = folder.data.folderName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun DeckInfo(
    navController: NavController,
    decks: UiState<List<Deck>>,
    selectedDecks: Set<Int>,
    isEditModeEnabled: Boolean,
    folderViewModel: FolderViewModel,
) {
    when (decks) {
        is UiState.Success -> {
            DisplayItemCount(
                plurals = R.plurals.deck_amount,
                count = decks.data.size,
                textStyle = MaterialTheme.typography.bodyMedium
            )
            LazyColumn(modifier = Modifier) {
                items(items = decks.data, key = { it.deckId }) { deck ->
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
                                    NavigationRoute.DeckScreen.createRoute(
                                        deck.deckId
                                    )
                                )
                            },
                        showCount = false,
                        showEditMode = isEditModeEnabled,
                        isSelected = selectedDecks.contains(deck.deckId),
                        onCheckedChange = { folderViewModel.toggleDeckSelection(deck.deckId) },
                        displayItemData = DisplayItemData(
                            itemIcon = R.drawable.deck_icon,
                            numberOfCards = deck.deckId,
                            itemName = deck.deckName,
                        ),
                        displayItemStyle = DisplayItemStyle(
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                                dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                            ),
                            iconColor = MaterialTheme.colorScheme.outlineVariant,
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                    )
                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
                }
            }
        }
    }
}


@Composable
private fun FolderDialog(
    navController: NavController,
    selectedElement: Int?,
    dialogVisibleAnimation: Float,
    folder: UiState<Folder>,
    folders: UiState<List<Folder>>,
    validation: Boolean?,
    folderViewModel: FolderViewModel,
    dialogState: DialogState,
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
                dialogState.currentDialogType == DialogType.Rename || dialogState.currentDialogType == DialogType.Create -> {
                    FolderRenameOrCreateDialog(folder, dialogState, validation, folderViewModel)
                }

                (dialogState.currentDialogType == DialogType.Move || dialogState.currentDialogType == DialogType.MoveItemsAndDeleteItem) -> {
                    FolderMoveDialog(
                        dialogState,
                        folders,
                        selectedElement,
                        folder,
                        folderViewModel,
                        navController
                    )
                }

                dialogState.currentDialogType == DialogType.Delete -> {
                    FolderDeleteDialog(folder, folderViewModel, navController, dialogState)
                }
            }
        }
    }
}

@Composable
private fun FolderRenameOrCreateDialog(
    folder: UiState<Folder>,
    dialogState: DialogState,
    validation: Boolean?,
    folderViewModel: FolderViewModel
) {
    when (folder) {
        is UiState.Success -> {
            CreateItemDialog(
                titleDialog = if (dialogState.currentDialogType == DialogType.Rename) {
                    stringResource(R.string.rename_title_item_dialog)
                } else {
                    stringResource(R.string.create_item_dialog_text_creating_deck)
                },
                placeholder = stringResource(R.string.create_item_dialog_text_input_title_folder),
                buttonText = if (dialogState.currentDialogType == DialogType.Rename) {
                    stringResource(R.string.save_text)
                } else {
                    stringResource(R.string.create_item_dialog_text_create_deck)
                },
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
                        if (dialogState.currentDialogType == DialogType.Rename) {
                            folderViewModel.renameFolder(
                                newFolderName = dialogState.dialogStateData.text,
                                folderId = folder.data.folderId
                            )
                        } else {
                            folderViewModel.createDeck(
                                Deck(
                                    deckName = dialogState.dialogStateData.text,
                                    folderId = folder.data.folderId
                                )
                            )
                        }
                        dialogState.closeDialog()
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

@Composable
private fun FolderMoveDialog(
    dialogState: DialogState,
    folders: UiState<List<Folder>>,
    selectedElement: Int?,
    folder: UiState<Folder>,
    folderViewModel: FolderViewModel,
    navController: NavController
) {
    when (folder) {
        is UiState.Success -> {
            SelectItemDialog(
                titleDialog = stringResource(R.string.dialog_select_folder),
                dialogState = dialogState,
                selectItems = folders.mapToUiState { folderPairs ->
                    folderPairs.map { Pair(it.folderName, it.folderId) }
                },
                selectedElement = selectedElement,
                sourceLocation = folder.data.folderId,
                fetchList = { folderViewModel.getAllFolders() },
                onClickSave = {
                    handleSave(dialogState, selectedElement, folderViewModel, folder, navController)
                },
            )

        }
    }

}

private fun handleSave(
    dialogState: DialogState,
    selectedElement: Int?,
    folderViewModel: FolderViewModel,
    folder: UiState.Success<Folder>,
    navController: NavController
) {
    if (dialogState.currentDialogType == DialogType.Move) {
        selectedElement?.let {
            folderViewModel.moveDecksBetweenFolders(
                sourceFolderId = folder.data.folderId,
                targetFolderId = it,
                deckIds = folderViewModel.selectedDecks.value.sorted().toList()
            )
        }
        dialogState.closeDialog()
        folderViewModel.updateEditMode()
        folderViewModel.clearSelectDeck()
    } else if (dialogState.currentDialogType == DialogType.MoveItemsAndDeleteItem) {
        selectedElement?.let {
            folderViewModel.addDecksToFolder(
                targetFolderId = it,
                deckIds = folderViewModel.selectedDecks.value.sorted().toList()
            )
        }
        folderViewModel.deleteFolder(folder.data)
        folderViewModel.clearSelectDeck()
        dialogState.closeDialog()
        dialogState.stopSelectingDecksForMoveAndDelete()
        navController.popBackStack()
    }
}

@Composable
private fun FolderDeleteDialog(
    folder: UiState<Folder>,
    folderViewModel: FolderViewModel,
    navController: NavController,
    dialogState: DialogState
) {
    DeleteItemDialog(
        onClickDeleteAll = {
            folder.mapSuccess { folderViewModel.deleteFolder(it) }
            navController.popBackStack()
        },
        onClickDeletePartially = {
            folderViewModel.updateEditMode()
            dialogState.closeDialog()
            dialogState.startSelectingDecksForMoveAndDelete()
        }
    )
}

@Composable
private fun FolderDropdownMenu(
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
        ) { dropdownMenuState.toggle() }
    )
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