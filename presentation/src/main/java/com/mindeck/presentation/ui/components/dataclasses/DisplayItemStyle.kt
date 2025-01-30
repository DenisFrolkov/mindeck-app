package com.mindeck.presentation.ui.components.dataclasses

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class DisplayItemStyle(
    val backgroundColor: Color,
    val iconColor: Color,
    val textStyle: TextStyle,
    val textMaxLines: Int = 1
)
