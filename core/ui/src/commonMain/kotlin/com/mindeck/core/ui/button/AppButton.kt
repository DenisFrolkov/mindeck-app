package com.mindeck.core.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppButton(
    onAction: () -> Unit,
    buttonText: String,
    buttonIcon: DrawableResource?,
    color: Color,
) {
    Button(
        onClick = { onAction() },
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding =
            PaddingValues(
                horizontal = MindeckTheme.dimensions.spacingLg,
                vertical = MindeckTheme.dimensions.spacingMd,
            ),
    ) {
        buttonIcon?.let {
            Icon(
                painter = painterResource(buttonIcon),
                contentDescription = null,
            )
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
        }
        Text(
            text = buttonText,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
