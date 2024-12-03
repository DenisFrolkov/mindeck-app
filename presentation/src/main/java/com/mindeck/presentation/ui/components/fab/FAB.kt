package com.mindeck.presentation.ui.components.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun FAB(
    fabColor: Color,
    fabIconColor: Color,
    fabIcon: Painter,
    fabMenuItems: List<FabMenuData>,
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
            .background(color = fabColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                fabState.open()
            }
    ) {
        if (!fabState.isExpanded) {
            Icon(
                painter = fabIcon,
                contentDescription = "FAB",
                tint = fabIconColor,
                modifier = Modifier.size(20.dp).alpha(alphaFab)
            )
        } else {
            Column(modifier = Modifier.alpha(alphaMenu)) {
                FABMenu(fabState = fabState, listItemsMenu = fabMenuItems, textStyle = textStyle, iconColor = fabIconColor)
            }
        }
    }
}
