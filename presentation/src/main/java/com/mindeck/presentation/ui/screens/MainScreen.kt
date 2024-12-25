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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTracker
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTrackerState
import com.mindeck.presentation.ui.components.dialog.CreateItemDialog
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDialogCreateItem
import com.mindeck.presentation.ui.components.fab.FAB
import com.mindeck.presentation.ui.components.fab.FabMenuData
import com.mindeck.presentation.ui.components.fab.FabState
import com.mindeck.presentation.ui.components.fab.FabState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.components.folder.DisplayCardItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    val scrollState = rememberScrollState()

    var dropdownMenuState = remember { DropdownMenuState() }

    val dailyProgressTrackerState = remember {
        DailyProgressTrackerState(
            totalCards = 500,
            answeredCards = 30
        )
    }

    val dialogVisibleAnimation = animateDialogCreateItem(
        targetAlpha = dropdownMenuState.dialogAlpha,
        animationDuration = dropdownMenuState.animationDuration * 3
    )
    var folderName by remember { mutableStateOf("") }

    val MAX_DISPLAY_ITEMS = 5

    val folders = mainViewModel.folderUIState.collectAsState().value

    val fabMenuItems = listOf(
        FabMenuData(
            idItem = 0,
            text = stringResource(R.string.fab_menu_data_setting_list),
            icon = R.drawable.fab_open_menu_setting_icon,
            navigation = { }
        ),
        FabMenuData(
            idItem = 1,
            text = stringResource(R.string.fab_menu_data_create_folder_list),
            icon = R.drawable.fab_open_menu_create_folder_icon,
            navigation = {
                dropdownMenuState.openCreateDialog()
            }
        ),
        FabMenuData(
            idItem = 2,
            text = stringResource(R.string.fab_menu_data_create_card_list),
            icon = R.drawable.fab_open_menu_create_card_icon,
            navigation = { navController.navigate(NavigationRoute.CreationCardScreen.route) }
        )
    )

    val fabState = remember { FabState(expandedHeight = ITEM_HEIGHT.dp * fabMenuItems.size) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(state = scrollState)
            .statusBarsPadding()
            .padding(all = dimenDpResource(R.dimen.padding_medium))
    ) {
        DailyProgressTracker(
            dptIcon = painterResource(R.drawable.dpt_icon),
            dailyProgressTrackerState = dailyProgressTrackerState
        )

        Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))

        when (folders) {
            is UiState.Success -> {
                folders.data.take(MAX_DISPLAY_ITEMS).forEach { folder ->
                    DisplayCardItem(
                        showCount = false,
                        itemIcon =
                        painterResource(R.drawable.folder_icon),
                        numberOfCards = folder.folderId,
                        itemName = folder.folderName,
                        backgroundColor = MaterialTheme.colorScheme.outlineVariant,
                        iconColor = MaterialTheme.colorScheme.secondary,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                dimenDpResource(R.dimen.border_width_dot_two_five),
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.small
                            )
                            .clip(shape = MaterialTheme.shapes.small)
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
                if (folders.data.size > MAX_DISPLAY_ITEMS) {

                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {

                        Box(modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigate(NavigationRoute.FoldersScreen.route)
                            }) {
                            Text(
                                text = stringResource(R.string.title_text_all_folders),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.padding(
                                    vertical = dimenDpResource(R.dimen.padding_medium),
                                    horizontal = dimenDpResource(R.dimen.padding_extra_large)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
    if (fabState.isExpanded) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                fabState.reset()
            }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(dimenDpResource(R.dimen.padding_medium))
            .wrapContentSize(Alignment.BottomEnd)
    ) {
        FAB(
            fabColor = MaterialTheme.colorScheme.outlineVariant,
            fabIconColor = MaterialTheme.colorScheme.onPrimary,
            fabIcon = painterResource(R.drawable.fab_menu_icon),
            fabMenuItems = fabMenuItems,
            fabState = fabState,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }

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
            CreateItemDialog(
                titleDialog = stringResource(R.string.create_item_dialog_text_creating_folder),
                placeholder = stringResource(R.string.create_item_dialog_text_input_title_folder),
                buttonText = stringResource(R.string.create_item_dialog_text_create_folder),
                value = folderName,
                onValueChange = { newValue -> folderName = newValue },
                onBackClick = {
                    dropdownMenuState.closeDialog()
                },
                onClickButton = {
                    mainViewModel.createFolder(Folder(folderName = folderName))
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