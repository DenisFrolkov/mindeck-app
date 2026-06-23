package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.add_a_photo_icon
import mindeck_app.feature.card.generated.resources.create_card_photo_hint
import mindeck_app.feature.card.generated.resources.create_card_photo_title
import mindeck_app.feature.card.generated.resources.create_card_section_photo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun PhotoPick(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            stringResource(CreateCardRes.string.create_card_section_photo),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingSm)

        val dashedBorderColor = MaterialTheme.colorScheme.outline
        val dashedCornerRadius = MindeckTheme.dimensions.spacingLg
        val dashedStrokeWidth = MindeckTheme.dimensions.borderThick
        val dashLengthWidth = MindeckTheme.dimensions.dash
        val dashGapWidth = MindeckTheme.dimensions.dash

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MindeckTheme.dimensions.spacingLg))
                    .clickable(onClick = onClick)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(MindeckTheme.dimensions.spacingLg),
                    ).drawBehind {
                        drawRoundRect(
                            color = dashedBorderColor,
                            cornerRadius = CornerRadius(dashedCornerRadius.toPx()),
                            style =
                                Stroke(
                                    width = dashedStrokeWidth.toPx(),
                                    pathEffect =
                                        PathEffect.dashPathEffect(
                                            floatArrayOf(dashLengthWidth.toPx(), dashGapWidth.toPx()),
                                            0f,
                                        ),
                                ),
                        )
                    },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VerticalSpacer(MindeckTheme.dimensions.spacingXl)
            Box(
                modifier =
                    Modifier.background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape =
                            RoundedCornerShape(
                                MindeckTheme.dimensions.spacingLg,
                            ),
                    ),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_a_photo_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier =
                        Modifier
                            .padding(MindeckTheme.dimensions.spacingMd)
                            .size(MindeckTheme.dimensions.iconMd),
                )
            }
            VerticalSpacer(MindeckTheme.dimensions.spacingXs)
            Text(
                text = stringResource(CreateCardRes.string.create_card_photo_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXs)
            Text(
                text = stringResource(CreateCardRes.string.create_card_photo_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXl)
        }
    }
}
