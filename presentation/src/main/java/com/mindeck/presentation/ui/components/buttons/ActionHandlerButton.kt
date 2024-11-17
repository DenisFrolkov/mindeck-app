package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.mindeck.presentation.ui.theme.White

@Composable
fun ActionHandlerButton(iconPainter: Painter, contentDescription: String, iconTint: Color = White, boxModifier: Modifier, iconModifier: Modifier) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = boxModifier
    ) {
        Icon(
            painter = iconPainter,
            tint = iconTint,
            contentDescription = contentDescription,
            modifier = iconModifier
        )
    }
}