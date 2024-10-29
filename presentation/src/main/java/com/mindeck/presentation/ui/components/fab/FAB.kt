package com.mindeck.presentation.ui.components.fab

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun FAB(
    fabColor: Color,
    fabIconColor: Color,
    fabIcon: Painter,
    fabMenuItems: List<FabMenuDataClass>,
    fabState: FabState,
) {
    val fabWidthAnimate by animateDpAsState(
        targetValue = fabState.fabWidth,
        animationSpec = tween(durationMillis = fabState.animationDuration),
        label = "Fab Width Animation"
    )
    val fabHeightAnimate by animateDpAsState(
        targetValue = fabState.fabHeight,
        animationSpec = tween(durationMillis = fabState.animationDuration),
        label = "Fab Height Animation"
    )
    val fabShapeAnimate by animateDpAsState(
        targetValue = fabState.fabShape,
        animationSpec = tween(durationMillis = fabState.animationDuration),
        label = "Fab Shape Animation"
    )
    val alphaMenu by animateFloatAsState(
        targetValue = fabState.menuAlphaValue,
        animationSpec = tween(durationMillis = fabState.animationDuration + fabState.animationDuration),
        label = "Fab Menu Alpha"
    )
    val alphaFab by animateFloatAsState(
        targetValue = fabState.fabAlphaValue,
        animationSpec = tween(durationMillis = fabState.animationDuration + fabState.animationDuration),
        label = "Fab Menu Alpha"
    )

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
        when {
            fabState.isExpanded == false -> {
                Icon(
                    painter = fabIcon,
                    contentDescription = "FAB",
                    tint = fabIconColor,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(alphaFab)
                )
            }

            fabState.isExpanded == true -> {
                Column(
                    modifier = Modifier
                        .alpha(alphaMenu)
                ) {
                    FABMenu(listItemsMenu = fabMenuItems)
                }
            }
        }
    }
}
