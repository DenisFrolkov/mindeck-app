package com.mindeck.core.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppTextButton(
    onAction: () -> Unit,
    buttonText: String,
    buttonIcon: DrawableResource?,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.primary,
) {
    TextButton(
        onClick = onAction,
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
