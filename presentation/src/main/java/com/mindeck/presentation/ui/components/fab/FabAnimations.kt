package com.mindeck.presentation.ui.components.fab

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp

@Composable
fun animateFabWidth(targetWidth: Dp, animationDuration: Int): Dp {
    val fabWidthAnimate by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = tween(durationMillis = animationDuration),
        label = "Fab Width Animation"
    )
    return fabWidthAnimate
}

@Composable
fun animateFabHeight(targetHeight: Dp, animationDuration: Int): Dp {
    val fabHeightAnimate by animateDpAsState(
        targetValue = targetHeight,
        animationSpec = tween(durationMillis = animationDuration),
        label = "Fab Height Animation"
    )
    return fabHeightAnimate
}

@Composable
fun animateFabShape(targetShape: Dp, animationDuration: Int): Dp {
    val fabShapeAnimate by animateDpAsState(
        targetValue = targetShape,
        animationSpec = tween(durationMillis = animationDuration),
        label = "Fab Shape Animation"
    )
    return fabShapeAnimate
}

@Composable
fun animateMenuAlpha(targetAlpha: Float, animationDuration: Int): Float {
    val alphaMenu by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = animationDuration),
        label = "Fab Menu Alpha Animation"
    )
    return alphaMenu
}

@Composable
fun animateFabAlpha(targetAlpha: Float, animationDuration: Int): Float {
    val alphaFab by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = animationDuration),
        label = "Fab Alpha Animation"
    )
    return alphaFab
}
