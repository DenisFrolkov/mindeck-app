package com.mindeck.presentation.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.onError
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.theme.text_white

@Composable
fun EditElementModalWindow(
    titleText: String,
    buttonText: String,
    listItem: List<ChooseElement>,
    isInputValid: UiState<Unit?>,
    isEditModeEnabled: Boolean,
    selectedItems: Int?,
    exitButton: () -> Unit,
    saveButton: (Int) -> Unit,
    onClickItem: (id: Int) -> Unit = {},
    onEditItem: (id: Int) -> Unit,
) {
    val isLoading by remember(isInputValid) { derivedStateOf { isInputValid is UiState.Loading } }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(MaterialTheme.shapes.small)
                .padding(dimenDpResource(R.dimen.card_input_field_item_padding)),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ActionHandlerButton(
                    iconPainter = painterResource(R.drawable.back_icon),
                    contentDescription = stringResource(R.string.back_screen_icon_button),
                    iconTint = MaterialTheme.colorScheme.onPrimary,
                    onClick = exitButton,
                )
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimenDpResource(R.dimen.padding_small))
                    .height(dimenDpResource(R.dimen.display_card_item_min_size) * if (listItem.size < 3) listItem.size.toFloat() else 2.8f)
                    .border(
                        width = dimenDpResource(R.dimen.border_width_dot_five),
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
            ) {
                items(listItem) { item ->
                    DisplayItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                dimenDpResource(R.dimen.border_width_dot_two_five),
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.extraSmall,
                            )
                            .clip(shape = MaterialTheme.shapes.extraSmall)
                            .height(dimenDpResource(R.dimen.display_card_item_min_size))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                onClickItem(item.id)
                            },
                        showCount = false,
                        showEditMode = isEditModeEnabled,
                        isSelected = selectedItems == item.id,
                        onCheckedChange = { onEditItem(item.id) },
                        displayItemData = DisplayItemData(
                            itemIcon = R.drawable.card_icon,
                            itemName = item.title,
                        ),
                        displayItemStyle = DisplayItemStyle(
                            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                                dimenFloatResource(R.dimen.float_zero_dot_five_significance),
                            ),
                            iconColor = MaterialTheme.colorScheme.outlineVariant,
                            textStyle = MaterialTheme.typography.bodyMedium,
                        ),
                    )
                }
            }

            isInputValid.onError { message ->
                Text(
                    message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimenDpResource(R.dimen.spacer_extra_small)),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                )
            }

            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimenDpResource(R.dimen.circular_progress_indicator_top_padding))
                        .wrapContentSize(Alignment.Center),
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one),
                    )
                }
            } else {
                SaveDataButton(
                    text = buttonText,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = text_white,
                    ),
                    buttonModifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            saveButton(0)
                        },
                )
            }
        }
    }
}
