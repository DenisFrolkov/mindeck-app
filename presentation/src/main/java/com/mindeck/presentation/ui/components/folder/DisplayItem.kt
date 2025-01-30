package com.mindeck.presentation.ui.components.folder

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.formatNumber

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DisplayItem(
    modifier: Modifier = Modifier,
    showCount: Boolean,
    showEditMode: Boolean = false,
    isSelected: Boolean = false,
    onCheckedChange: () -> Unit = {},
    displayItemData: DisplayItemData,
    displayItemStyle: DisplayItemStyle,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (showCount) {
            Text(
                text = formatNumber(displayItemData.numberOfCards),
                style = displayItemStyle.textStyle,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .size(dimenDpResource(R.dimen.display_card_item_size))
                    .wrapContentSize(Alignment.Center)
            )

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = dimenDpResource(R.dimen.vertical_divider_height),
                color = MaterialTheme.colorScheme.outline
            )
        }

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = dimenDpResource(R.dimen.padding_extra_small))
        ) {
            Text(
                text = displayItemData.itemName,
                maxLines = displayItemStyle.textMaxLines,
                overflow = TextOverflow.Ellipsis,
                style = displayItemStyle.textStyle,
            )
        }

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = dimenDpResource(R.dimen.vertical_divider_height),
            color = MaterialTheme.colorScheme.outline
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(dimenDpResource(R.dimen.display_card_item_size))
                .background(color = displayItemStyle.backgroundColor)
        ) {
            AnimatedContent(
                modifier = Modifier
                    .size(dimenDpResource(R.dimen.display_card_item_animation_size))
                    .align(Alignment.Center),
                targetState = showEditMode,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) with fadeOut(animationSpec = tween(200))
                }
            ) { editModeEnabled ->
                if (editModeEnabled) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            onCheckedChange()
                        },
                        colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.primary)
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(dimenDpResource(R.dimen.icon_size)),
                        painter = painterResource(displayItemData.itemIcon),
                        tint = displayItemStyle.iconColor,
                        contentDescription = stringResource(R.string.folder_icon)
                    )
                }
            }
        }
    }
}