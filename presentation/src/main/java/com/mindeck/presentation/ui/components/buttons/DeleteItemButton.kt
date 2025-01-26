package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeleteItemButton(titleButton: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = MaterialTheme.shapes.medium
        )
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onClick()
        }
    ) {
        Text(
            text = titleButton,
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 10.dp
            )
        )
    }
}