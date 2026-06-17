package com.mindeck.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimensions(
    // Spacing scale (4dp base grid)
    val spacingNone: Dp = 0.dp,
    val spacingXxs: Dp = 2.dp,
    val spacingXs: Dp = 4.dp,
    val spacingSm: Dp = 8.dp,
    val spacingMd: Dp = 12.dp,
    val spacingLg: Dp = 16.dp,
    val spacingXl: Dp = 20.dp,
    val spacingXxl: Dp = 24.dp,
    val spacingXxxl: Dp = 32.dp,
    // Semantic aliases
    val screenPadding: Dp = 16.dp,
    val itemGap: Dp = 8.dp,
    val sectionGap: Dp = 18.dp,
    val fabMargin: Dp = 20.dp,
    val touchTarget: Dp = 48.dp,
    // Icon sizes
    val iconXs: Dp = 18.dp,
    val iconSm: Dp = 22.dp,
    val iconMd: Dp = 24.dp,
    val iconLg: Dp = 36.dp,
    val iconXl: Dp = 48.dp,
    // Border / stroke widths
    val borderThin: Dp = 1.dp,
    val borderThick: Dp = 2.dp,
    val dash: Dp = 6.dp,
    val dash2: Dp = 4.dp,
)

val LocalDimensions = staticCompositionLocalOf { AppDimensions() }
