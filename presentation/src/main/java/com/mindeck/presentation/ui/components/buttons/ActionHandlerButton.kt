package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.mindeck.presentation.ui.theme.on_primary_white

@Composable
fun ActionHandlerButton(
    iconPainter: Painter,
    contentDescription: String,
    iconTint: Color = on_primary_white,
    onClick: () -> Unit,
    iconModifier: Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onClick()
        }
    ) {
        Icon(
            painter = iconPainter,
            tint = iconTint,
            contentDescription = contentDescription,
            modifier = iconModifier
        )
    }
}