package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showBackground = true)
@Composable
private fun CardInputFieldEmptyPreview() {
    MindeckTheme {
        CardInputField(
            placeholder = "Введите вопрос...",
            value = "",
            onValueChange = {},
            placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.large,
                )
                .border(
                    MindeckTheme.dimensions.dp0_25,
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.large,
                )
                .padding(MindeckTheme.dimensions.paddingSm),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardInputFieldFilledPreview() {
    MindeckTheme {
        CardInputField(
            value = "Что такое sealed class?",
            onValueChange = {},
            placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.large,
                )
                .border(
                    MindeckTheme.dimensions.dp0_25,
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.large,
                )
                .padding(MindeckTheme.dimensions.paddingSm),
        )
    }
}
