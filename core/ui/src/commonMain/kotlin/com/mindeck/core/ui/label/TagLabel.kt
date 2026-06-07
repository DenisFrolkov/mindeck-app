package com.mindeck.core.ui.label

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mindeck.core.ui.theme.MindeckTheme

@Composable
fun TagLabel(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs),
    ) {
        Box(
            modifier =
                Modifier
                    .background(
                        color,
                        shape = MaterialTheme.shapes.medium,
                    ).size(MindeckTheme.dimensions.spacingMd),
        )
        Text(text = text, style = MaterialTheme.typography.titleSmall, color = color)
    }
}
