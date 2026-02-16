package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import com.mindeck.presentation.R

@Composable
fun ActionHandlerButton(
    iconPainter: Painter,
    iconTint: Color,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .clickable {
                onClick()
            },
    ) {
        Icon(
            painter = iconPainter,
            tint = iconTint,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.action_handler_icon_padding))
                .size(dimensionResource(R.dimen.action_handler_size)),
        )
    }
}
