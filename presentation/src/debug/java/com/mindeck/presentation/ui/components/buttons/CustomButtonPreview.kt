package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showBackground = true)
@Composable
private fun CustomButtonPreview() {
    MindeckTheme {
        CustomButton(
            text = "Сохранить",
            color = MaterialTheme.colorScheme.primary,
            onClick = {},
            modifier = Modifier.size(
                height = MindeckTheme.dimensions.dp42,
                width = MindeckTheme.dimensions.dp140,
            ),
        )
    }
}
