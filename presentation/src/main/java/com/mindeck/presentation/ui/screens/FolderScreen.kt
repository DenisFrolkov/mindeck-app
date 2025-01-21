package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.dialog.CreateItemDialog
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.DisplayItemCount
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
import com.mindeck.presentation.viewmodel.FolderViewModel

@Composable
fun FolderScreen(
    navController: NavController,
    folderViewModel: FolderViewModel,
) {
    var dropdownMenuState = remember { DropdownMenuState() }
    var dialogState = remember { DialogState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )
    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dialogState.dialogAlpha,
        animationDuration = dialogState.animationDuration * 3
    )

    val folder = folderViewModel.folderUIState.collectAsState().value
    val decks = folderViewModel.deckByIdrUIState.collectAsState().value

    val validation = dialogState.validation

    var listDropdownMenu =
        dropdownMenuDataList(dialogState, folderViewModel, navController)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            topBar = {
                FolderTopBar(navController, dropdownMenuState)
            },
            content = { padding ->
                Content(
                    padding,
                    folder,
                    decks,
                    navController,
                    dropdownMenuState,
                    listDropdownMenu,
                    dropdownVisibleAnimation
                )
            }
        )

        FolderDialog(dialogState, dialogVisibleAnimation, folder, validation, folderViewModel)
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
    padding: PaddingValues,
    folder: UiState<Folder>,
    decks: UiState<List<Deck>>,
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
        FolderInfo(folder, decks)
        DeckInfo(decks, navController)
    }
    FolderDropdownMenu(dropdownMenuState, listDropdownMenu, padding, dropdownVisibleAnimation)
}

@Composable
private fun dropdownMenuDataList(
    dialogState: DialogState,
    folderViewModel: FolderViewModel,
    navController: NavController
): List<DropdownMenuData> {
    val folder = folderViewModel.folderUIState.collectAsState().value
    return listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_rename_list),
            action = {
                dialogState.openRenameDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_deck_list),
            action = {
                dialogState.openCreateDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remote_list),
            action = {
                when (folder) {
                    is UiState.Success -> {
                        folderViewModel.deleteFolder(folder = folder.data)
                        navController.popBackStack()
                    }
                }
            }
        )
    )
}


@Composable
private fun FolderInfo(
    folder: UiState<Folder>,
    decks: UiState<List<Deck>>
) {
    when (folder) {
        is UiState.Success -> {
            Text(
                text = folder.data.folderName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
            Spacer(Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
            when (decks) {
                is UiState.Success -> {
                    DisplayItemCount(
                        plurals = R.plurals.deck_amount,
                        count = decks.data.size,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun DeckInfo(
    decks: UiState<List<Deck>>,
    navController: NavController
) {
    when (decks) {
        is UiState.Success -> {
            LazyColumn(modifier = Modifier) {
                items(items = decks.data, key = { it.deckId }) { deck ->
                    DisplayCardItem(
                        showCount = false,
                        itemIcon = painterResource(R.drawable.deck_icon),
                        numberOfCards = deck.deckId,
                        itemName = deck.deckName,
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
                                    NavigationRoute.DeckScreen.createRoute(
                                        deck.deckId
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
private fun FolderDialog(
    dialogState: DialogState,
    dialogVisibleAnimation: Float,
    folder: UiState<Folder>,
    validation: Boolean?,
    folderViewModel: FolderViewModel
) {
    if (dialogState.isOpeningDialog) {
        Box(modifier = Modifier.alpha(dialogVisibleAnimation)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.outline.copy(dimenFloatResource(R.dimen.float_zero_dot_five_significance)))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
            )
            when (folder) {
                is UiState.Success -> {
                    CreateItemDialog(
                        titleDialog = if (dialogState.isOpeningRenameDialog) {
                            stringResource(R.string.rename_title_item_dialog)
                        } else {
                            stringResource(R.string.create_item_dialog_text_creating_deck)
                        },
                        placeholder = stringResource(R.string.create_item_dialog_text_input_title_folder),
                        buttonText = if (dialogState.isOpeningRenameDialog) {
                            stringResource(R.string.save_text)
                        } else {
                            stringResource(R.string.create_item_dialog_text_create_deck)
                        },
                        value = dialogState.isEnterDialogText,
                        validation = validation == true || validation == null,
                        onValueChange = { newValue ->
                            dialogState.validationCreate(newValue)
                            dialogState.isEnterDialogText = newValue
                        },
                        onBackClick = {
                            dialogState.closeDialog()
                        },
                        onClickButton = {
                            if (dialogState.validationCreate(dialogState.isEnterDialogText)) {
                                if (dialogState.isOpeningRenameDialog) {
                                    folderViewModel.renameFolder(
                                        newFolderName = dialogState.isEnterDialogText,
                                        folderId = folder.data.folderId
                                    )
                                } else {
                                    folderViewModel.createDeck(
                                        Deck(
                                            deckName = dialogState.isEnterDialogText,
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
    }
}

@Composable
private fun FolderDropdownMenu(
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