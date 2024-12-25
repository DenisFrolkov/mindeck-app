package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.DeckViewModel
import com.mindeck.presentation.viewmodel.FolderViewModel

@Composable
fun FolderScreen(
    navController: NavController,
    folderViewModel: FolderViewModel,
    deckViewModel: DeckViewModel
) {
    var dropdownMenuState = remember { DropdownMenuState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )
    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dropdownMenuState.dialogAlpha,
        animationDuration = dropdownMenuState.animationDuration * 3
    )

    val folder = folderViewModel.folderUIState.collectAsState().value
    val decks = folderViewModel.deckByIdrUIState.collectAsState().value

    val deleteFolderData =
        remember { mutableStateOf(Folder(folderId = 0, folderName = "")) }

    var name by remember { mutableStateOf("") }

    var listDropdownMenu = listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_rename_list),
            action = {
                dropdownMenuState.openRenameDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_deck_list),
            action = {
                dropdownMenuState.openCreateDialog()
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remote_list),
            action = {
                folderViewModel.deleteFolder(deleteFolderData.value)
                navController.popBackStack()
            }
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
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
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                when (folder) {
                    is UiState.Success -> {
                        deleteFolderData.value = folder.data
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
    )

    if (dropdownMenuState.isOpeningDialog) {
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
                        titleDialog = if (dropdownMenuState.isOpeningRenameDialog) {
                            stringResource(R.string.rename_title_item_dialog)
                        } else {
                            stringResource(R.string.create_item_dialog_text_creating_deck)
                        },
                        placeholder = stringResource(R.string.create_item_dialog_text_input_title_folder),
                        buttonText = if (dropdownMenuState.isOpeningRenameDialog) {
                            stringResource(R.string.save_text)
                        } else {
                            stringResource(R.string.create_item_dialog_text_create_deck)
                        },
                        value = name,
                        onValueChange = { newValue -> name = newValue },
                        onBackClick = {
                            dropdownMenuState.closeDialog()
                        },
                        onClickButton = {
                            if (dropdownMenuState.isOpeningRenameDialog) {
                                folderViewModel.renameFolder(
                                    newFolderName = name,
                                    folderId = folder.data.folderId
                                )
                            } else {
                                folderViewModel.createDeck(
                                    Deck(
                                        deckName = name,
                                        folderId = folder.data.folderId
                                    )
                                )
                            }

                            dropdownMenuState.closeDialog()
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
