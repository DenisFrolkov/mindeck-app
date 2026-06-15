package com.mindeck.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.button.AppTextButton
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.add_icon
import mindeck_app.core.ui.generated.resources.download_icon
import mindeck_app.core.ui.generated.resources.lightbulb_icon
import mindeck_app.core.ui.generated.resources.style_icon
import mindeck_app.feature.home.generated.resources.home_empty_create_card
import mindeck_app.feature.home.generated.resources.home_empty_hint
import mindeck_app.feature.home.generated.resources.home_empty_import_deck
import mindeck_app.feature.home.generated.resources.home_empty_subtitle
import mindeck_app.feature.home.generated.resources.home_empty_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.home.generated.resources.Res as HomeRes

@Composable
internal fun HomeEmpty(
    onCreateCard: () -> Unit,
    onImportDeck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier.background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.style_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier =
                        Modifier
                            .padding(MindeckTheme.dimensions.spacingXxl)
                            .size(MindeckTheme.dimensions.iconXl),
                )
            }
            VerticalSpacer(MindeckTheme.dimensions.spacingXxxl)
            Text(
                text = stringResource(HomeRes.string.home_empty_title),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingSm)
            Text(
                text = stringResource(HomeRes.string.home_empty_subtitle),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = MindeckTheme.dimensions.spacingXxxl),
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXxxl)
            AppButton(
                onAction = onCreateCard,
                buttonText = stringResource(HomeRes.string.home_empty_create_card),
                buttonIcon = Res.drawable.add_icon,
                color = MaterialTheme.colorScheme.primary,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXs)
            AppTextButton(
                onAction = onImportDeck,
                buttonText = stringResource(HomeRes.string.home_empty_import_deck),
                buttonIcon = Res.drawable.download_icon,
            )
        }

        Card(
            shape = MindeckTheme.shapes.card,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            Row(
                modifier =
                    Modifier.padding(
                        horizontal = MindeckTheme.dimensions.spacingLg,
                        vertical = MindeckTheme.dimensions.spacingMd,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.lightbulb_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
                Text(
                    text = stringResource(HomeRes.string.home_empty_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
                )
            }
        }
    }
}
