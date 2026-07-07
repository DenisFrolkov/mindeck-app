package com.mindeck.feature.card.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.format.formatFileSize
import com.mindeck.core.ui.format.toLabel
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.card.model.DraftImage
import kotlinx.coroutines.delay
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.add_a_photo_icon
import mindeck_app.core.ui.generated.resources.close_icon
import mindeck_app.core.ui.generated.resources.delete_icon
import mindeck_app.core.ui.generated.resources.image_icon
import mindeck_app.feature.card.generated.resources.create_card_image_cancel_load
import mindeck_app.feature.card.generated.resources.create_card_image_preview
import mindeck_app.feature.card.generated.resources.create_card_image_remove
import mindeck_app.feature.card.generated.resources.create_card_photo_hint
import mindeck_app.feature.card.generated.resources.create_card_photo_title
import mindeck_app.feature.card.generated.resources.create_card_section_photo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

private const val PREVIEW_ASPECT_RATIO = 16f / 9f
private const val CANCEL_BUTTON_DELAY_MS = 2000L

@Composable
internal fun PhotoPick(
    image: DraftImage?,
    isLoading: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    onCancelLoad: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            stringResource(CreateCardRes.string.create_card_section_photo),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingSm)

        when {
            isLoading -> PhotoLoading(onCancel = onCancelLoad)
            image != null -> PhotoPreview(image = image, onRemove = onRemove)
            else -> PhotoEmpty(onClick = onClick)
        }
    }
}

@Composable
private fun PhotoPreview(
    image: DraftImage,
    onRemove: () -> Unit,
) {
    val bitmap = remember(image) { runCatching { image.bytes.decodeToImageBitmap() }.getOrNull() }
    val sizeLabel = formatFileSize(image.bytes.size).toLabel()
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(PREVIEW_ASPECT_RATIO)
                .clip(RoundedCornerShape(MindeckTheme.dimensions.spacingLg))
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        bitmap?.let {
            Image(
                bitmap = bitmap,
                contentDescription = stringResource(CreateCardRes.string.create_card_image_preview),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().padding(MindeckTheme.dimensions.spacingSm),
            contentAlignment = Alignment.TopEnd,
        ) {
            ScrimIconButton(
                icon = Res.drawable.delete_icon,
                contentDescription = stringResource(CreateCardRes.string.create_card_image_remove),
                onClick = onRemove,
            )
        }
        Box(
            modifier = Modifier.fillMaxHeight().padding(MindeckTheme.dimensions.spacingSm),
            contentAlignment = Alignment.BottomStart,
        ) {
            Row(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.45f))
                        .padding(MindeckTheme.dimensions.spacingXs),
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXs),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.image_icon),
                    contentDescription = null,
                    tint = MindeckTheme.extraColors.onScrim,
                    modifier = Modifier.size(MindeckTheme.dimensions.spacingLg),
                )
                Text(
                    text = sizeLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MindeckTheme.extraColors.onScrim,
                )
            }
        }
    }
}

@Composable
private fun PhotoLoading(onCancel: () -> Unit) {
    var showCancelButton by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(CANCEL_BUTTON_DELAY_MS)
        showCancelButton = true
    }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(PREVIEW_ASPECT_RATIO)
                .clip(RoundedCornerShape(MindeckTheme.dimensions.spacingLg))
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        if (showCancelButton) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(MindeckTheme.dimensions.spacingSm),
                contentAlignment = Alignment.TopEnd,
            ) {
                ScrimIconButton(
                    icon = Res.drawable.close_icon,
                    contentDescription = stringResource(CreateCardRes.string.create_card_image_cancel_load),
                    onClick = onCancel,
                )
            }
        }
    }
}

@Composable
private fun ScrimIconButton(
    icon: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.45f))
                .clickable(onClick = onClick)
                .padding(MindeckTheme.dimensions.spacingSm),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = MindeckTheme.extraColors.onScrim,
            modifier = Modifier.size(MindeckTheme.dimensions.iconXs),
        )
    }
}

@Composable
private fun PhotoEmpty(onClick: () -> Unit) {
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
                    shape = RoundedCornerShape(MindeckTheme.dimensions.spacingLg),
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
