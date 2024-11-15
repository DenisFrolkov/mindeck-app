package com.mindeck.presentation.ui.components.dropdown_selector

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun animateDropdownWidth(targetHeight: Dp, animationDuration: Int): Dp {
    val animetedHeightIn by animateDpAsState(
        targetValue = targetHeight,
        animationSpec = tween(durationMillis = animationDuration)
    )
    return animetedHeightIn
}

@Composable
fun animateDropdownOffsetY(dropdownOffsetY: Dp, animationDuration: Int): Dp {
    val animetedHeightIn by animateDpAsState(
        targetValue = dropdownOffsetY,
        animationSpec = tween(durationMillis = animationDuration)
    )
    return animetedHeightIn
}

@Composable
fun animateDropdownAlpha(dropdownAlpha: Float, animationDuration: Int): Float {
    val animetedAlpha by animateFloatAsState(
        targetValue = dropdownAlpha,
        animationSpec = tween(durationMillis = animationDuration)
    )

    return animetedAlpha
}


