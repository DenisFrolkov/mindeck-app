package com.mindeck.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class SpecialAppShapes(
    val card: RoundedCornerShape = RoundedCornerShape(20.dp),
    val avatar: RoundedCornerShape = RoundedCornerShape(14.dp),
    val hero: RoundedCornerShape = RoundedCornerShape(28.dp),
    val photo: RoundedCornerShape = RoundedCornerShape(24.dp),
    val pill: RoundedCornerShape = RoundedCornerShape(100.dp),
    val segmentStart: RoundedCornerShape = RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50),
    val segmentEnd: RoundedCornerShape = RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50),
)

val LocalShapes = staticCompositionLocalOf { SpecialAppShapes() }
