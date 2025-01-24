package com.mindeck.presentation.ui.components.dialog

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.theme.background_light_blue
import com.mindeck.presentation.ui.theme.scrim_black
import com.mindeck.presentation.ui.theme.text_white
import com.mindeck.presentation.viewmodel.FolderViewModel

@Composable
fun SelectItemDialog(
    dialogState: DialogState,
    folderViewModel: FolderViewModel,
    folders: UiState<List<Folder>>,
    selectFolder: Int?,
    folderId: Int
) {
    LaunchedEffect(dialogState.isOpeningMoveDialog) {
        folderViewModel.getAllFolders()
    }

    when (folders) {
        is UiState.Success -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight(dimenFloatResource(R.dimen.alpha_menu_dialog_height))
                    .padding(horizontal = dimenDpResource(R.dimen.card_input_field_background_horizontal_padding))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(
                            color = background_light_blue,
                            shape = MaterialTheme.shapes.small
                        )
                        .clip(MaterialTheme.shapes.small)
                        .padding(dimenDpResource(R.dimen.card_input_field_item_padding))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ActionHandlerButton(
                            iconPainter = painterResource(R.drawable.back_icon),
                            contentDescription = stringResource(R.string.back_screen_icon_button),
                            iconTint = MaterialTheme.colorScheme.onPrimary,
                            onClick = { dialogState.closeMoveDialog() },
                            iconModifier = Modifier
                                .clip(shape = MaterialTheme.shapes.extraLarge)
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                                .padding(dimenDpResource(R.dimen.padding_small))
                                .size(dimenDpResource(R.dimen.padding_medium)),
                        )
                        Text(
                            text = "Выберите папку",
                            style = MaterialTheme.typography.titleMedium,
                            color = scrim_black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
                    LazyColumn(modifier = Modifier.heightIn(max = 115.dp)) {
                        items(folders.data) { data ->
                            if (data.folderId != folderId) {
                                SelectItem(selectFolder == data.folderId, folderViewModel, data)
                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .background(Color.Red)
                            .height(dimensionResource(R.dimen.spacer_medium))
                    )
                    AnimatedVisibility(
                        visible = selectFolder != null
                    ) {
                        SaveDataButton(
                            text = "Сохранить",
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = text_white
                            ),
                            buttonModifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    folderViewModel.moveDecksBetweenFolders(
                                        sourceFolderId = folderId,
                                        targetFolderId = selectFolder!!,
                                        deckIds = folderViewModel.selectedDecks.value.sorted().toList()
                                    )
                                    dialogState.closeMoveDialog()
                                    folderViewModel.updateEditMode()
                                    folderViewModel.updateSelectFolder(null)
                                    folderViewModel.clearSelectDeck()
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectItem(
    selectFolder: Boolean,
    folderViewModel: FolderViewModel,
    data: Folder
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.secondary.copy(
                    alpha = if (selectFolder) 1f else .25f
                )
            )
            .border(
                dimenDpResource(R.dimen.border_width_dot_two_five),
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.extraSmall
            )
            .clickable {
                if (selectFolder) {
                    folderViewModel.updateSelectFolder(null)
                } else {
                    folderViewModel.updateSelectFolder(data.folderId)
                }
            }
    ) {
        Text(
            text = data.folderName,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp
            ),
            modifier = Modifier
                .padding(start = dimenDpResource(R.dimen.padding_extra_small))
                .padding(
                    vertical = dimenDpResource(
                        R.dimen.padding_extra_small
                    )
                )
        )
    }
}