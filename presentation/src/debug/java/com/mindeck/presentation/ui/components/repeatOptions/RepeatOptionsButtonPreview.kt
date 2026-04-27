package com.mindeck.presentation.ui.components.repeatOptions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showBackground = true)
@Composable
private fun RepeatOptionsButtonPreview() {
    MindeckTheme {
        RepeatOptionsButton(
            buttonColor = Color(0xFFB5EAD7),
            label = "Хорошо",
            time = "1 д",
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = {},
        )
    }
}
