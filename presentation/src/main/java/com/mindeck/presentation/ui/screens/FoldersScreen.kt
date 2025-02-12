package com.mindeck.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.dialog.data_class.CreateItemDialog
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.dialog.DialogState
import com.mindeck.presentation.ui.components.dialog.animateDialogCreateItem
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.animateToastItem
import com.mindeck.presentation.ui.components.dialog.data_class.DialogType
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.viewmodel.FoldersViewModel
import kotlinx.coroutines.delay

@Composable
fun FoldersScreen(
    navController: NavController,
) {
    val foldersViewModel: FoldersViewModel = hiltViewModel(navController.currentBackStackEntry!!)

    val folders = foldersViewModel.foldersState.collectAsState().value

    var dialogState = remember { DialogState() }

    val validation = dialogState.dialogStateData.isValid
    val toastMessage = dialogState.toastTextEvent
    val toastValue = dialogState.toastBooleanEvent

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

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            topBar = {
                FoldersEditTopBar(navController, dialogState)
            },
            content = { padding ->
                Content(padding, folders, navController)
            }
        )
        if (dialogState.isDialogVisible && dialogState.currentDialogType == DialogType.Create) {
            FoldersDialog(
                dialogVisibleAnimation = dialogVisibleAnimation,
                validation = validation,
                dialogState = dialogState,
                foldersViewModel = foldersViewModel
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
private fun FoldersEditTopBar(
    navController: NavController,
    dialogState: DialogState
) {
    Box(
        modifier = Modifier
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
    ) {
        ActionBar(
            onBackClick = { navController.popBackStack() },
            onMenuClick = { dialogState.openCreateDialog() },
            menuIcon = R.drawable.create_folder,
            containerModifier = Modifier
                .fillMaxWidth(),
            iconModifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(all = dimenDpResource(R.dimen.padding_small))
                .size(size = dimenDpResource(R.dimen.padding_medium)),
        )
    }
}

@Composable
private fun Content(
    padding: PaddingValues,
    folders: UiState<List<Folder>>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
    ) {
        FoldersInfo(folders, navController)
    }
}

@Composable
private fun FoldersInfo(
    folders: UiState<List<Folder>>,
    navController: NavController
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
                        folders.data.size,
                        folders.data.size
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
            LazyColumn {
                items(
                    items = folders.data,
                    key = { it.folderId }) { folder ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                dimenDpResource(R.dimen.border_width_dot_two_five),
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
                            },
                        showCount = false,
                        displayItemData = DisplayItemData(
                            itemIcon = R.drawable.folder_icon,
                            numberOfCards = folder.folderId,
                            itemName = folder.folderName,
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
                stringResource(R.string.error_get_all_folders),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}

@Composable
private fun FoldersDialog(
    dialogVisibleAnimation: Float,
    validation: Boolean?,
    dialogState: DialogState,
    foldersViewModel: FoldersViewModel
) {
    Box(modifier = Modifier.alpha(dialogVisibleAnimation)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(dialogState.scrimDialogAlpha))
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
                    foldersViewModel.createFolder(folderName = dialogState.dialogStateData.text)
                    dialogState.closeDialog()
                }
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