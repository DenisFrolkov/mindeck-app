package com.mindeck.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.button.appButtonColors
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.cloud_off_icon
import mindeck_app.core.ui.generated.resources.refresh_icon
import mindeck_app.feature.home.generated.resources.home_error_retry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.home.generated.resources.Res as HomeRes

@Composable
internal fun HomeError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                MindeckTheme.dimensions.spacingLg,
                Alignment.CenterVertically,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier.background(
                    MaterialTheme.colorScheme.errorContainer,
                    shape = CircleShape,
                ),
        ) {
            Icon(
                painter = painterResource(Res.drawable.cloud_off_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier =
                    Modifier
                        .padding(MindeckTheme.dimensions.spacingXxl)
                        .size(MindeckTheme.dimensions.iconLg),
            )
        }
        Text(
            text = message,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )
        AppButton(
            onAction = onRetry,
            buttonText = stringResource(HomeRes.string.home_error_retry),
            buttonIcon = Res.drawable.refresh_icon,
            colors =
                appButtonColors(
                    container = MaterialTheme.colorScheme.errorContainer,
                    content = MaterialTheme.colorScheme.onErrorContainer,
                ),
        )
    }
}
