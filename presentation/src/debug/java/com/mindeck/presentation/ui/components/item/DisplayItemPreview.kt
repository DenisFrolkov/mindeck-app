package com.mindeck.presentation.ui.components.item

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview(showBackground = true)
@Composable
private fun DisplayItemPreview() {
    MindeckTheme {
        DisplayItem(
            modifier = Modifier.padding(MindeckTheme.dimensions.paddingMd),
            icon = R.drawable.card_icon,
            name = "Английский язык — базовый курс",
            onClick = {},
        )
    }
}
