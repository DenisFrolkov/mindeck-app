package com.mindeck.core.ui.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mindeck.core.ui.button.AppButton
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMediaBottomSheet(
    title: String,
    description: String,
    sources: List<MediaSource>,
    linkSectionLabel: String,
    linkValue: String,
    onLinkValueChange: (String) -> Unit,
    linkPlaceholder: String,
    confirmLabel: String,
    onConfirmLink: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    linkIcon: DrawableResource? = null,
    chevronIcon: DrawableResource? = null,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MindeckTheme.dimensions.spacingXl,
                        vertical = MindeckTheme.dimensions.spacingLg,
                    ),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingXxs)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            VerticalSpacer(MindeckTheme.dimensions.spacingLg)
            sources.forEachIndexed { index, source ->
                if (index > 0) VerticalSpacer(MindeckTheme.dimensions.spacingSm)
                MediaSourceRow(source = source, chevronIcon = chevronIcon)
            }

            VerticalSpacer(MindeckTheme.dimensions.spacingLg)
            Text(
                text = linkSectionLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            VerticalSpacer(MindeckTheme.dimensions.spacingSm)
            LinkInputRow(
                value = linkValue,
                onValueChange = onLinkValueChange,
                placeholder = linkPlaceholder,
                confirmLabel = confirmLabel,
                onConfirm = onConfirmLink,
                linkIcon = linkIcon,
            )
        }
    }
}

@Composable
private fun MediaSourceRow(
    source: MediaSource,
    chevronIcon: DrawableResource?,
) {
    var click by remember { mutableStateOf(false) }

    val swatchColor =
        if (click) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }
    val iconColor =
        if (click) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onPrimaryContainer
        }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(MindeckTheme.shapes.card)
                .clickable(
                    onClick = {
                        click = true
                        source.onClick()
                    },
                ).padding(MindeckTheme.dimensions.spacingSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(MindeckTheme.dimensions.iconXl)
                    .clip(MindeckTheme.shapes.avatar)
                    .background(swatchColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(source.icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(MindeckTheme.dimensions.iconMd),
            )
        }
        HorizontalSpacer(MindeckTheme.dimensions.spacingMd)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = source.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = source.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        chevronIcon?.let {
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(MindeckTheme.dimensions.iconSm),
            )
        }
    }
}

@Composable
private fun LinkInputRow(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    linkIcon: DrawableResource?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.weight(1f),
            textStyle =
                MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            decorationBox = { inner ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .border(
                                width = MindeckTheme.dimensions.borderThin,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MindeckTheme.shapes.avatar,
                            ).padding(
                                horizontal = MindeckTheme.dimensions.spacingMd,
                                vertical = MindeckTheme.dimensions.spacingMd,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    linkIcon?.let {
                        Icon(
                            painter = painterResource(it),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(MindeckTheme.dimensions.iconXs),
                        )
                        HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        inner()
                    }
                }
            },
        )
        AppButton(
            onAction = onConfirm,
            buttonText = confirmLabel,
            shape = MaterialTheme.shapes.medium,
        )
    }
}
