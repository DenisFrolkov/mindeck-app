package com.mindeck.core.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Immutable
data class AppButtonColors(
    val container: Color,
    val content: Color,
)

@Composable
fun appButtonColors(
    container: Color = MaterialTheme.colorScheme.primary,
    content: Color = MaterialTheme.colorScheme.onPrimary,
): AppButtonColors = AppButtonColors(container = container, content = content)

@Composable
fun AppButton(
    onAction: () -> Unit,
    buttonText: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    buttonIcon: DrawableResource? = null,
    colors: AppButtonColors = appButtonColors(),
    shape: Shape = ButtonDefaults.shape,
) {
    Button(
        onClick = onAction,
        shape = shape,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.container,
            contentColor = colors.content,
        ),
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
                modifier = Modifier.size(MindeckTheme.dimensions.iconSm),
            )
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
        }
        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
