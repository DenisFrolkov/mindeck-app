package com.mindeck.core.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.DeckSwatchColors
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.create_deck_action_cancel
import mindeck_app.core.ui.generated.resources.create_deck_action_create
import mindeck_app.core.ui.generated.resources.create_deck_color_label
import mindeck_app.core.ui.generated.resources.create_deck_dialog_description
import mindeck_app.core.ui.generated.resources.create_deck_dialog_title
import mindeck_app.core.ui.generated.resources.create_deck_name_label
import mindeck_app.core.ui.generated.resources.create_deck_name_placeholder
import mindeck_app.core.ui.generated.resources.library_add_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val DECK_NAME_MAX_LENGTH = 60
private const val PREVIEW_LETTER = 'A'

@Composable
fun CreateDeckDialog(
    colors: List<DeckSwatchColors>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, colorIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    maxNameLength: Int = DECK_NAME_MAX_LENGTH,
) {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableIntStateOf(0) }
    val canCreate = name.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MindeckTheme.shapes.hero,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Column(modifier = Modifier.padding(MindeckTheme.dimensions.spacingXl)) {
                DialogHeader()
                VerticalSpacer(MindeckTheme.dimensions.spacingMd)
                Text(
                    text = stringResource(Res.string.create_deck_dialog_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                VerticalSpacer(MindeckTheme.dimensions.spacingLg)
                Text(
                    text = stringResource(Res.string.create_deck_name_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                VerticalSpacer(MindeckTheme.dimensions.spacingXs)
                DeckNameField(
                    value = name,
                    onValueChange = { if (it.length <= maxNameLength) name = it },
                    maxLength = maxNameLength,
                )

                VerticalSpacer(MindeckTheme.dimensions.spacingLg)
                Text(
                    text = stringResource(Res.string.create_deck_color_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                VerticalSpacer(MindeckTheme.dimensions.spacingSm)
                val previewLetter = name.firstOrNull()?.uppercaseChar() ?: PREVIEW_LETTER
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
                ) {
                    itemsIndexed(colors) { index, swatch ->
                        DeckColorSwatch(
                            swatch = swatch,
                            letter = previewLetter,
                            selected = index == selectedColor,
                            onClick = { selectedColor = index },
                        )
                    }
                }

                VerticalSpacer(MindeckTheme.dimensions.spacingLg)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DialogTextButton(
                        text = stringResource(Res.string.create_deck_action_cancel),
                        onClick = onDismiss,
                    )
                    DialogTextButton(
                        text = stringResource(Res.string.create_deck_action_create),
                        onClick = { onConfirm(name.trim(), selectedColor) },
                        enabled = canCreate,
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogTextButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    TextButton(onClick = onClick, enabled = enabled) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun DialogHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier =
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MindeckTheme.shapes.avatar,
                    ).padding(MindeckTheme.dimensions.spacingSm),
        ) {
            Icon(
                painter = painterResource(Res.drawable.library_add_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(MindeckTheme.dimensions.iconMd),
            )
        }
        HorizontalSpacer(MindeckTheme.dimensions.spacingMd)
        Text(
            text = stringResource(Res.string.create_deck_dialog_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun DeckNameField(
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle =
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        decorationBox = { inner ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .border(
                            width = MindeckTheme.dimensions.borderThick,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MindeckTheme.shapes.avatar,
                        ).padding(
                            horizontal = MindeckTheme.dimensions.spacingMd,
                            vertical = MindeckTheme.dimensions.spacingLg,
                        ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.create_deck_name_placeholder),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    inner()
                }
                HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
                Text(
                    text = "${value.length} / $maxLength",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
private fun DeckColorSwatch(
    swatch: DeckSwatchColors,
    letter: Char,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = MaterialTheme.shapes.large
    Box(
        modifier =
            Modifier
                .size(MindeckTheme.dimensions.iconXl)
                .clip(shape)
                .clickable(onClick = onClick)
                .then(
                    if (selected) {
                        Modifier
                            .border(MindeckTheme.dimensions.borderThick, swatch.border, shape)
                            .padding(MindeckTheme.dimensions.spacingXxs)
                    } else {
                        Modifier
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(swatch.container, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = swatch.onContainer,
            )
        }
    }
}
