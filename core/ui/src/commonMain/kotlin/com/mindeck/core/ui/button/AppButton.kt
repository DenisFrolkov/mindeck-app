package com.mindeck.core.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
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
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Button(
        onClick = onAction,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding =
            PaddingValues(
                horizontal = MindeckTheme.dimensions.spacingLg,
                vertical = MindeckTheme.dimensions.spacingMd,
            ),
        modifier = modifier,
    ) {
        buttonIcon?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(MindeckTheme.dimensions.iconSm),
            )
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
        }
        Text(
            text = buttonText,
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
