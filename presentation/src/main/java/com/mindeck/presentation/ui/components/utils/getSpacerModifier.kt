package com.mindeck.presentation.ui.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mindeck.presentation.ui.theme.outline_medium_gray

fun getSpacerModifier(color: Color = outline_medium_gray, width: Dp) = Modifier
    .fillMaxHeight()
    .width(width)
    .background(color = color)