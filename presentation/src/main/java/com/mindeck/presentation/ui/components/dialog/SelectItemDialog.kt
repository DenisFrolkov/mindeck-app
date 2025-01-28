package com.mindeck.presentation.ui.components.dialog

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.theme.text_white

@Composable
fun SelectItemDialog(
    titleDialog: String,
    dialogState: DialogState,
    selectedElement: Int?,
    sourceLocation: Int,
    selectItems: UiState<List<Pair<String, Int>>>,
    fetchList: () -> Unit,
    onClickSave: () -> Unit,
) {
    LaunchedEffect(dialogState.isOpeningMoveDialog) {
        fetchList()
    }

    when (selectItems) {
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
                            color = MaterialTheme.colorScheme.background,
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
                            onClick = { dialogState.toggleMoveDialog() },
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
                            text = titleDialog,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
                    if (selectItems.data.size > 1) {
                        LazyColumn(modifier = Modifier.heightIn(max = dimenDpResource(R.dimen.dialog_list_move_item_max_height))) {
                            items(selectItems.data) { item ->
                                if (item.second != sourceLocation) {
                                    SelectItem(
                                        itemSelected = selectedElement == item.second,
                                        item = item,
                                        onItemSelected = {
                                            if (selectedElement == item.second) {
                                                dialogState.updateSelectItem(null)
                                            } else {
                                                dialogState.updateSelectItem(item.second)
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))
                                }
                            }
                        }
                    } else {
                        Text(
                            stringResource(R.string.warning_no_elements),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(start = dimenDpResource(R.dimen.padding_extra_small))
                                .padding(
                                    vertical = dimenDpResource(
                                        R.dimen.padding_extra_small
                                    )
                                )
                        )
                    }
                    AnimatedVisibility(
                        visible = selectedElement != null
                    ) {
                        SaveDataButton(
                            text = stringResource(R.string.save_text),
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
                                    onClickSave()
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
    itemSelected: Boolean,
    item: Pair<String, Int>,
    onItemSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.secondary.copy(
                    alpha = if (itemSelected) dimenFloatResource(R.dimen.dialog_background_selected_item) else dimenFloatResource(
                        R.dimen.dialog_background_unselected_item
                    )
                )
            )
            .border(
                dimenDpResource(R.dimen.border_width_dot_two_five),
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.extraSmall
            )
            .clickable {
                onItemSelected()
            }
    ) {
        Text(
            text = item.first,
            style = MaterialTheme.typography.bodyLarge,
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