package com.mindeck.presentation.ui.components.selector

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showBackground = true)
@Composable
private fun SelectorRowPreview() {
    MindeckTheme {
        SelectorRow(
            modifier = Modifier.padding(MindeckTheme.dimensions.paddingMd),
            label = "Колода",
            selectedText = "Английский",
            onClick = {},
        )
    }
}
