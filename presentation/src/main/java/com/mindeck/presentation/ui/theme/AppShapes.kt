package com.mindeck.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(
    val shapeXs: RoundedCornerShape = RoundedCornerShape(4.dp),
    val shapeSm: RoundedCornerShape = RoundedCornerShape(6.dp),
    val shapeMd: RoundedCornerShape = RoundedCornerShape(10.dp),
    val shapeLg: RoundedCornerShape = RoundedCornerShape(12.dp),
    val shapeXl: RoundedCornerShape = RoundedCornerShape(28.dp),
)

val LocalShapes = staticCompositionLocalOf { AppShapes() }
