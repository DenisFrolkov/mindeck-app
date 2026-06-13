package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.card.model.TextFormat
import mindeck_app.core.ui.generated.resources.format_bold_icon
import mindeck_app.core.ui.generated.resources.format_italic_icon
import mindeck_app.core.ui.generated.resources.format_list_bulleted_icon
import mindeck_app.core.ui.generated.resources.format_list_numbered_icon
import mindeck_app.core.ui.generated.resources.format_strikethrough_icon
import mindeck_app.core.ui.generated.resources.format_underlined_icon
import mindeck_app.feature.card.generated.resources.create_card_format_bold
import mindeck_app.feature.card.generated.resources.create_card_format_bullet_list
import mindeck_app.feature.card.generated.resources.create_card_format_italic
import mindeck_app.feature.card.generated.resources.create_card_format_numbered_list
import mindeck_app.feature.card.generated.resources.create_card_format_strikethrough
import mindeck_app.feature.card.generated.resources.create_card_format_underline
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.core.ui.generated.resources.Res as CoreRes
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun TextFormattingToolbar(
    active: Set<TextFormat>,
    onToggle: (TextFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextFormat.entries.forEach { format ->
            val item = format.toolbarItem()
            FormatButton(
                icon = item.icon,
                contentDescription = stringResource(item.contentDescription),
                selected = format in active,
                onClick = { onToggle(format) },
            )
            if (format == TextFormat.STRIKETHROUGH) {
                VerticalDivider(
                    modifier = Modifier.height(MindeckTheme.dimensions.iconSm),
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
private fun FormatButton(
    icon: DrawableResource,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(
                    if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainer
                    },
                ).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint =
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            modifier = Modifier.padding(MindeckTheme.dimensions.spacingMd).size(MindeckTheme.dimensions.iconXs),
        )
    }
}

private data class ToolbarItem(
    val icon: DrawableResource,
    val contentDescription: StringResource,
)

private fun TextFormat.toolbarItem(): ToolbarItem =
    when (this) {
        TextFormat.BOLD ->
            ToolbarItem(CoreRes.drawable.format_bold_icon, CreateCardRes.string.create_card_format_bold)
        TextFormat.ITALIC ->
            ToolbarItem(CoreRes.drawable.format_italic_icon, CreateCardRes.string.create_card_format_italic)
        TextFormat.UNDERLINE ->
            ToolbarItem(CoreRes.drawable.format_underlined_icon, CreateCardRes.string.create_card_format_underline)
        TextFormat.STRIKETHROUGH ->
            ToolbarItem(CoreRes.drawable.format_strikethrough_icon, CreateCardRes.string.create_card_format_strikethrough)
        TextFormat.BULLET_LIST ->
            ToolbarItem(CoreRes.drawable.format_list_bulleted_icon, CreateCardRes.string.create_card_format_bullet_list)
        TextFormat.NUMBERED_LIST ->
            ToolbarItem(CoreRes.drawable.format_list_numbered_icon, CreateCardRes.string.create_card_format_numbered_list)
    }
