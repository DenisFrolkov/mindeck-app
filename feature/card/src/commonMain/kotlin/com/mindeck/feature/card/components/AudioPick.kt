package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.close_icon
import mindeck_app.core.ui.generated.resources.graphic_eq_icon
import mindeck_app.core.ui.generated.resources.play_arrow_icon
import mindeck_app.core.ui.generated.resources.upload_file_icon
import mindeck_app.feature.card.generated.resources.create_card_audio_play
import mindeck_app.feature.card.generated.resources.create_card_audio_remove
import mindeck_app.feature.card.generated.resources.create_card_audio_speak
import mindeck_app.feature.card.generated.resources.create_card_audio_upload
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.core.ui.generated.resources.Res as CoreRes
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun AudioPick(
    selectedAudio: String?,
    onPickFile: () -> Unit,
    onRemoveAudio: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardCornerRadius = MindeckTheme.dimensions.spacingXxl
    val dashedBorderColor = MaterialTheme.colorScheme.outline
    val dashedStrokeWidth = MindeckTheme.dimensions.borderThin
    val dashLengthWidth = MindeckTheme.dimensions.spacingXs
    val dashGapWidth = MindeckTheme.dimensions.spacingXs

    val containerModifier =
        if (selectedAudio == null) {
            Modifier
                .clip(RoundedCornerShape(MindeckTheme.dimensions.spacingXxl))
                .drawBehind {
                    drawRoundRect(
                        color = dashedBorderColor,
                        cornerRadius = CornerRadius(cardCornerRadius.toPx()),
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
                }.clickable { onPickFile() }
        } else {
            Modifier
                .clip(RoundedCornerShape(cardCornerRadius))
                .background(MaterialTheme.colorScheme.primaryContainer)
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .then(containerModifier)
                .padding(MindeckTheme.dimensions.spacingMd),
    ) {
        if (selectedAudio == null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AudioIconCircle(icon = CoreRes.drawable.graphic_eq_icon)
                    HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
                    Text(
                        text = stringResource(CreateCardRes.string.create_card_audio_speak),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingMd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.upload_file_icon),
                        contentDescription = stringResource(CreateCardRes.string.create_card_audio_upload),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .padding(MindeckTheme.dimensions.spacingSm)
                                .size(MindeckTheme.dimensions.iconSm),
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AudioIconCircle(
                    icon = CoreRes.drawable.play_arrow_icon,
                    contentDescription = stringResource(CreateCardRes.string.create_card_audio_play),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                )
                Text(
                    text = "0:02",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                )
                HorizontalSpacer(MindeckTheme.dimensions.spacingMd)
                Icon(
                    painter = painterResource(CoreRes.drawable.close_icon),
                    contentDescription = stringResource(CreateCardRes.string.create_card_audio_remove),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onRemoveAudio)
                            .padding(MindeckTheme.dimensions.spacingSm)
                            .size(MindeckTheme.dimensions.iconSm),
                )
            }
        }
    }
}

@Composable
private fun AudioIconCircle(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(containerColor)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = contentColor,
            modifier =
                Modifier
                    .padding(MindeckTheme.dimensions.spacingMd)
                    .size(MindeckTheme.dimensions.iconSm),
        )
    }
}
