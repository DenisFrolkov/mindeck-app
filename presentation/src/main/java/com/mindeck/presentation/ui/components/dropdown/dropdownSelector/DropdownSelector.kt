package com.mindeck.presentation.ui.components.dropdown.dropdownSelector

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.theme.outline_medium_gray
import com.mindeck.presentation.ui.theme.text_black

@Composable
fun DropdownSelector(
    label: String,
    selectedItem: Pair<String, Int?>,
    itemsState: List<Pair<String, Int>>,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    val dropdownSelectorState = remember { DropdownSelectorState() }

    Row {
        Text(
            text = label,
            style = textStyle,
            modifier = Modifier
                .padding(dimenDpResource(R.dimen.padding_extra_small))
                .wrapContentSize(Alignment.CenterStart)
                .width(dimenDpResource(R.dimen.dropdown_min_weight)),
        )

        Spacer(modifier = Modifier.width(dimenDpResource(R.dimen.spacer_small)))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    onClick()
                    dropdownSelectorState.toggle()
                },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.extraSmall,
                    )
                    .height(dimenDpResource(R.dimen.dropdown_menu_item_height))
                    .border(
                        dimenDpResource(R.dimen.border_width_dot_two_five),
                        color = MaterialTheme.colorScheme.outline,
                        shape = if (dropdownSelectorState.isExpanded) {
                            RoundedCornerShape(
                                topStart = dimenDpResource(R.dimen.text_input_topStart_padding),
                                topEnd = dimenDpResource(R.dimen.text_input_topEnd_padding),
                            )
                        } else MaterialTheme.shapes.extraSmall,
                    )
                    .wrapContentSize(Alignment.Center),
            ) {
                Text(
                    text = selectedItem.first,
                    style = textStyle.copy(color = text_black),
                )
            }

            if (dropdownSelectorState.isExpanded) {
                if (itemsState.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .border(
                                dimenDpResource(R.dimen.border_width_dot_two_five),
                                MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(
                                    bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_padding),
                                    bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_padding),
                                ),
                            ),
                    ) {
                        SelectorDropdownMenu(
                            selectorItemList = itemsState,
                            onItemClick = onItemClick,
                            dropdownSelectorState = dropdownSelectorState,
                            textStyle = textStyle,
                        )
                    }
                } else {
                    EmptyDropdownMessage(
                        message = "Создать папку",
                        textStyle = textStyle,
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyDropdownMessage(message: String, textStyle: TextStyle) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimenDpResource(R.dimen.dropdown_menu_item_height))
            .border(
                dimenDpResource(R.dimen.border_width_dot_two_five),
                MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(
                    bottomStart = dimenDpResource(R.dimen.text_input_topStart_padding),
                    bottomEnd = dimenDpResource(R.dimen.text_input_topEnd_padding),
                ),
            )
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(
                    bottomStart = dimenDpResource(R.dimen.dropdown_menu_item_bottomStart_padding),
                    bottomEnd = dimenDpResource(R.dimen.dropdown_menu_item_bottomEnd_padding),
                ),
            ),
    ) {
        Text(text = message, style = textStyle)
    }
}

@Composable
fun SelectorDropdownMenu(
    selectorItemList: List<Pair<String, Int>>,
    dropdownSelectorState: DropdownSelectorState,
    onItemClick: (Pair<String, Int>) -> Unit,
    textStyle: TextStyle,
) {
    val animatedHeightIn = animateDropdownSelectorHeightIn(
        dropdownSelectorState.dropdownHeight,
        dropdownSelectorState.animationDuration,
    )
    val offsetY = animateDropdownSelectorOffsetY(
        dropdownSelectorState.dropdownOffsetY,
        dropdownSelectorState.animationDuration,
    )
    val alpha = animateDropdownSelectorAlpha(
        dropdownSelectorState.dropdownAlpha,
        dropdownSelectorState.animationDuration,
    )

    val baseItemModifier = Modifier
        .fillMaxWidth()
        .height(dimenDpResource(R.dimen.dropdown_menu_item_height))
        .alpha(alpha)
        .offset(y = -offsetY)

    LaunchedEffect(selectorItemList) {
        dropdownSelectorState.open()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = animatedHeightIn),
    ) {
        items(selectorItemList) {
            val isLastItem = it == selectorItemList.last()

            Box(
                contentAlignment = Alignment.Center,
                modifier = baseItemModifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = (
                            if (isLastItem) {
                                RoundedCornerShape(
                                    bottomStart = dimenDpResource(R.dimen.dropdown_menu_item_bottomStart_padding),
                                    bottomEnd = dimenDpResource(R.dimen.dropdown_menu_item_bottomEnd_padding),
                                )
                            } else {
                                RoundedCornerShape(size = dimenDpResource(R.dimen.text_input_bottomEnd_zero_padding))
                            }
                            ),
                    )
                    .drawBehind {
                        val borderThickness = 0.25.dp.toPx()
                        drawLine(
                            color = outline_medium_gray,
                            start = Offset(0f, size.height - borderThickness / 2),
                            end = Offset(size.width, size.height - borderThickness / 2),
                            strokeWidth = borderThickness,
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onItemClick(it)
                        dropdownSelectorState.reset()
                    },
            ) {
                Text(
                    it.first,
                    style = textStyle,
                )
            }
        }
    }
}
