package com.mindeck.presentation.ui.components.dropdown.dropdown_selector

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.mindeck.presentation.R

@Composable
fun animateDropdownHeightIn(targetHeight: Dp, animationDuration: Int): Dp {
    val animatedHeightIn by animateDpAsState(
        targetValue = targetHeight,
        animationSpec = tween(durationMillis = animationDuration), label = stringResource(R.string.label_animated_dropdown_height_in)
    )
    return animatedHeightIn
}

@Composable
fun animateDropdownOffsetY(dropdownOffsetY: Dp, animationDuration: Int): Dp {
    val animatedHeightIn by animateDpAsState(
        targetValue = dropdownOffsetY,
        animationSpec = tween(durationMillis = animationDuration), label = stringResource(R.string.label_animated_dropdown_offset_y)
    )
    return animatedHeightIn
}


@Composable
fun animateDropdownAlpha(dropdownAlpha: Float, animationDuration: Int): Float {
    val animatedAlpha by animateFloatAsState(
        targetValue = dropdownAlpha,
        animationSpec = tween(durationMillis = animationDuration), label = stringResource(R.string.label_animated_dropdown_alpha)
    )
    return animatedAlpha
}


