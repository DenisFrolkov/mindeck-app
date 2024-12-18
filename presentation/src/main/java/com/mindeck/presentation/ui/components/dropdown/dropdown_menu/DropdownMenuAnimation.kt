package com.mindeck.presentation.ui.components.dropdown.dropdown_menu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.mindeck.presentation.R

@Composable
fun animateDropdownMenuHeightIn(targetAlpha: Float, animationDuration: Int): Float {
    val animatedFloatIn by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = animationDuration), label = stringResource(R.string.label_animated_dropdown_menu_alpha)
    )
    return animatedFloatIn
}

@Composable
fun animateDialogCreateItem(targetAlpha: Float, animationDuration: Int): Float {
    val animatedFloatIn by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = animationDuration), label = stringResource(R.string.label_animated_dialog_create_item)
    )
    return animatedFloatIn
}

