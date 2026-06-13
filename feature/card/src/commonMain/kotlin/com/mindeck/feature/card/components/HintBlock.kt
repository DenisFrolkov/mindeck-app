package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import mindeck_app.feature.card.generated.resources.create_card_hint_placeholder
import mindeck_app.feature.card.generated.resources.create_card_section_hint
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun HintBlock(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            stringResource(CreateCardRes.string.create_card_section_hint),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingSm)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(MindeckTheme.shapes.avatar),
            textStyle = MaterialTheme.typography.bodyLarge,
            minLines = 4,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MindeckTheme.dimensions.spacingMd, vertical = MindeckTheme.dimensions.spacingLg),
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(CreateCardRes.string.create_card_hint_placeholder),
                                style =
                                    MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                    ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        innerTextField()
                    }
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(MindeckTheme.dimensions.borderThick)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                }
            },
        )
    }
}
