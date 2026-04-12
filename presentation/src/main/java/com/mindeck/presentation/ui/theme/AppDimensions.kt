package com.mindeck.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimensions(
    // Padding
    val paddingXs: Dp = 8.dp,
    val paddingSm: Dp = 12.dp,
    val paddingMd: Dp = 16.dp,
    val paddingLg: Dp = 24.dp,
    val paddingXl: Dp = 32.dp,

    // Spacer
    val spacerXs: Dp = 4.dp,
    val spacerSm: Dp = 6.dp,
    val spacerMd: Dp = 12.dp,
    val spacerLg: Dp = 20.dp,
    val spacerXl: Dp = 50.dp,

    // Icon
    val iconXs: Dp = 18.dp,
    val iconSm: Dp = 22.dp,
    val iconMd: Dp = 24.dp,
    val iconLg: Dp = 36.dp,
    val iconXl: Dp = 48.dp,

    // Stroke
    val strokeThin: Dp = 0.25.dp,

    // Numeric
    val dp0: Dp = 0.dp,
    val dp0_25: Dp = 0.25.dp,
    val dp1: Dp = 1.dp,
    val dp2: Dp = 2.dp,
    val dp4: Dp = 4.dp,
    val dp6: Dp = 6.dp,
    val dp8: Dp = 8.dp,
    val dp10: Dp = 10.dp,
    val dp12: Dp = 12.dp,
    val dp14: Dp = 14.dp,
    val dp16: Dp = 16.dp,
    val dp18: Dp = 18.dp,
    val dp20: Dp = 20.dp,
    val dp22: Dp = 22.dp,
    val dp24: Dp = 24.dp,
    val dp28: Dp = 28.dp,
    val dp30: Dp = 30.dp,
    val dp36: Dp = 36.dp,
    val dp40: Dp = 40.dp,
    val dp42: Dp = 42.dp,
    val dp46: Dp = 46.dp,
    val dp48: Dp = 48.dp,
    val dp80: Dp = 80.dp,
    val dp140: Dp = 140.dp,
    val dp200: Dp = 200.dp,
)

val LocalDimensions = staticCompositionLocalOf { AppDimensions() }
