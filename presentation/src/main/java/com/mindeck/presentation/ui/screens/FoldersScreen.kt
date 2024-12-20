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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.CreateItemDialog
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.getPluralForm
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.FoldersViewModel

@Composable
fun FoldersScreen(
    navController: NavController,
    foldersViewModel: FoldersViewModel
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
    val folders = foldersViewModel.folderUIState.collectAsState().value
    var inputFolderName by remember { mutableStateOf("") }

    var listDropdownMenu = listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_rename_list),
            action = {
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remote_list),
            action = {
            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_create_folder_list),
            action = {
                dropdownMenuState.openDialog()
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
                    .size(size = dimenDpResource(R.dimen.icon_size)),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                when (folders) {
                    is UiState.Success -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        ) {
                            Text(
                                text = pluralStringResource(
                                    R.plurals.folder_amount,
                                    getPluralForm(folders.data.size),
                                    folders.data.size
                                ),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))

                        LazyColumn {
                            items(
                                items = folders.data,
                                key = { it.folderId }) { folder ->
                                DisplayCardItem(
                                    showCount = true,
                                    itemIcon = painterResource(R.drawable.folder_icon),
                                    numberOfCards = folder.folderId,
                                    itemName = folder.folderName,
                                    backgroundColor = outline_variant_blue,
                                    iconColor = repeat_button_light_blue,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            dimenDpResource(R.dimen.border_width),
                                            MaterialTheme.colorScheme.outline,
                                            MaterialTheme.shapes.medium
                                        )
                                        .clip(MaterialTheme.shapes.medium)
                                        .height(dimenDpResource(R.dimen.display_card_item_size))
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            navController.navigate(
                                                NavigationRoute.FolderScreen.createRoute(
                                                    folder.folderId
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
                        .padding(top = dimenDpResource(R.dimen.dropdown_menu_vertical_top_padding))
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
                    .background(MaterialTheme.colorScheme.scrim.copy(dropdownMenuState.scrimDialogAlpha))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {

                    }
            )
            CreateItemDialog(
                titleDialog = stringResource(R.string.create_item_dialog_text_creating_folder),
                placeholder = stringResource(R.string.create_item_dialog_text_input_name_folder),
                buttonText = stringResource(R.string.create_item_dialog_text_create_folder),
                value = inputFolderName,
                onValueChange = { newValue -> inputFolderName = newValue },
                onBackClick = {
                    dropdownMenuState.closeDialog()
                },
                onClickButton = {
                    foldersViewModel.createFolder(Folder(folderName = inputFolderName))
                    dropdownMenuState.closeDialog()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.CenterStart),
                iconModifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
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
