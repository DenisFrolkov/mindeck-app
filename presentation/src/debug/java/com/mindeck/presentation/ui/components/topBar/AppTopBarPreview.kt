package com.mindeck.presentation.ui.components.topBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showBackground = true)
@Composable
private fun AppTopBarWithMenuPreview() {
    MindeckTheme {
        AppTopBar(
            showMenuButton = true,
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTopBarWithoutMenuPreview() {
    MindeckTheme {
        AppTopBar(
            showMenuButton = false,
            onBackClick = {},
        )
    }
}
