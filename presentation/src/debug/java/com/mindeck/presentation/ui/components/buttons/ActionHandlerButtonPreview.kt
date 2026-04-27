package com.mindeck.presentation.ui.components.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.MindeckTheme

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
