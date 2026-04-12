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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun ActionHandlerButton(
    painter: Painter,
    tint: Color,
    size: Dp = MindeckTheme.dimensions.dp18,
    contentDescription: String? = null,
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
            painter = painter,
            tint = tint,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(MindeckTheme.dimensions.paddingSm)
                .size(size),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionHandlerButtonPreview() {
    MindeckTheme {
        ActionHandlerButton(
            painter = painterResource(R.drawable.img_back),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = {},
        )
    }
}
