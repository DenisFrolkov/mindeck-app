package com.mindeck.presentation.ui.components.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun AppDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = MindeckTheme.dimensions.dp20,
                vertical = MindeckTheme.dimensions.dp10,
            ),
    )
}
