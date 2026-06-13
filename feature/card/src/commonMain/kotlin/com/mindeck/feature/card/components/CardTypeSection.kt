package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.card.model.CardType
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.check_icon
import mindeck_app.core.ui.generated.resources.dashboard_customize_icon
import mindeck_app.feature.card.generated.resources.create_card_section_type
import mindeck_app.feature.card.generated.resources.create_card_type_complex
import mindeck_app.feature.card.generated.resources.create_card_type_simple
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun CardTypeSection(
    selectedType: CardType,
    onSelectType: (CardType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            stringResource(CreateCardRes.string.create_card_section_type),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingSm)

        Row(
            modifier =
                Modifier.fillMaxWidth().border(
                    width = MindeckTheme.dimensions.borderThin,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CardTypeOption(
                icon = Res.drawable.check_icon,
                label = stringResource(CreateCardRes.string.create_card_type_simple),
                shape = MindeckTheme.shapes.segmentStart,
                selected = selectedType == CardType.SIMPLE,
                onClick = { onSelectType(CardType.SIMPLE) },
                modifier = Modifier.weight(1f),
            )
            CardTypeOption(
                icon = Res.drawable.dashboard_customize_icon,
                label = stringResource(CreateCardRes.string.create_card_type_complex),
                shape = MindeckTheme.shapes.segmentEnd,
                selected = selectedType == CardType.COMPLEX,
                onClick = { onSelectType(CardType.COMPLEX) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CardTypeOption(
    icon: DrawableResource,
    label: String,
    shape: Shape,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor =
        if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    Box(
        modifier =
            modifier
                .clip(shape)
                .then(
                    if (selected) {
                        Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                    } else {
                        Modifier
                    },
                ).clickable(onClick = onClick)
                .padding(vertical = MindeckTheme.dimensions.spacingMd),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(MindeckTheme.dimensions.spacingLg),
            )
            HorizontalSpacer(MindeckTheme.dimensions.spacingXs)
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = contentColor,
            )
        }
    }
}
