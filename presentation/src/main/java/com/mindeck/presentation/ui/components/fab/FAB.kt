package com.mindeck.presentation.ui.components.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource

@Composable
fun FAB(
    fabIcon: Painter,
    fabMenuItems: List<FabMenuData>,
    fabColor: Color,
    fabIconColor: Color,
    fabState: FabState,
    textStyle: TextStyle,
) {
    val fabWidthAnimate = animateFabWidth(fabState.fabWidth, fabState.animationDuration)
    val fabHeightAnimate = animateFabHeight(fabState.fabHeight, fabState.animationDuration)
    val fabShapeAnimate = animateFabShape(fabState.fabShape, fabState.animationDuration)
    val alphaMenu = animateMenuAlpha(fabState.menuAlphaValue, fabState.animationDuration * 2)
    val alphaFab = animateFabAlpha(fabState.fabAlphaValue, fabState.animationDuration * 2)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(fabShapeAnimate))
            .size(width = fabWidthAnimate, height = fabHeightAnimate)
            .background(fabColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                fabState.open()
            }
    ) {
        if (fabState.isExpanded) {
            Column(
                modifier = Modifier.alpha(alphaMenu)
            ) {
                fabMenuItems.forEachIndexed { index, menuItem ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimenDpResource(R.dimen.padding_extra_small))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                fabState.reset()
                                menuItem.navigation.invoke()
                            }
                    ) {
                        Text(
                            text = menuItem.text,
                            style = textStyle,
                            modifier = Modifier.padding(vertical = dimenDpResource(R.dimen.fab_menu_text_vertical_padding))
                        )
                        Icon(
                            painter = painterResource(menuItem.icon),
                            contentDescription = menuItem.iconContentDescription,
                            modifier = Modifier.size(dimenDpResource(R.dimen.fab_menu_icon_size)),
                            tint = fabIconColor
                        )
                    }
                    if (index != fabMenuItems.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = dimenDpResource(R.dimen.horizontal_divider_one_height),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        } else {
            Icon(
                painter = fabIcon,
                contentDescription = stringResource(R.string.floating_action_button),
                tint = fabIconColor,
                modifier = Modifier
                    .size(dimenDpResource(R.dimen.icon_size))
                    .alpha(alphaFab)
            )
        }
    }
}