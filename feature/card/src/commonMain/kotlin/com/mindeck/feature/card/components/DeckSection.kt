package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.card.model.DeckItem
import com.mindeck.feature.card.model.DeckPickState
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.action_add
import mindeck_app.core.ui.generated.resources.add_icon
import mindeck_app.core.ui.generated.resources.chevron_right_icon
import mindeck_app.core.ui.generated.resources.library_add_icon
import mindeck_app.core.ui.generated.resources.library_decks_icon
import mindeck_app.core.ui.generated.resources.stat_minus_icon
import mindeck_app.core.ui.generated.resources.style_icon
import mindeck_app.feature.card.generated.resources.create_card_deck_clear
import mindeck_app.feature.card.generated.resources.create_card_deck_create
import mindeck_app.feature.card.generated.resources.create_card_deck_pick
import mindeck_app.feature.card.generated.resources.create_card_section_deck
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun DeckSection(
    state: DeckPickState,
    onPick: () -> Unit,
    onClear: () -> Unit,
    onCreateDeck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dashedBorderColor = MaterialTheme.colorScheme.outline
    val dashedCornerRadius = MindeckTheme.shapes.borderShape
    val dashedStrokeWidth = MindeckTheme.dimensions.borderThick
    val dashLengthWidth = MindeckTheme.dimensions.dash2
    val dashGapWidth = MindeckTheme.dimensions.dash2

    Column(modifier = modifier) {
        Text(
            stringResource(CreateCardRes.string.create_card_section_deck),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VerticalSpacer(MindeckTheme.dimensions.spacingSm)
        Card(
            modifier = Modifier.fillMaxWidth().then(
                if (state == DeckPickState.NoDecks) {
                    Modifier.drawBehind {
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
                    }
                } else {
                    Modifier
                },
            ),
            shape = MindeckTheme.shapes.avatar,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            when (state) {
                DeckPickState.NoDecks -> NoDecksContent(onCreateDeck = onCreateDeck)
                DeckPickState.NotSelected -> PickPlaceholder(onPick = onPick)
                is DeckPickState.Selected -> SelectedDeck(deck = state.deck, onClear = onClear)
            }
        }
    }
}

@Composable
private fun NoDecksContent(onCreateDeck: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onCreateDeck)
                .padding(
                    horizontal = MindeckTheme.dimensions.spacingLg,
                    vertical = MindeckTheme.dimensions.spacingMd,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MindeckTheme.shapes.avatar
                )
                    .padding(MindeckTheme.dimensions.spacingSm)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.library_add_icon),
                    contentDescription = stringResource(Res.string.action_add),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconXs)
                )
            }
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
            Text(
                text = stringResource(CreateCardRes.string.create_card_deck_create),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Icon(
            painter = painterResource(Res.drawable.chevron_right_icon),
            contentDescription = stringResource(CreateCardRes.string.create_card_deck_clear),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(MindeckTheme.dimensions.iconSm),
        )
    }
}

@Composable
private fun SelectedDeck(
    deck: DeckItem,
    onClear: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClear)
                .padding(
                    horizontal = MindeckTheme.dimensions.spacingLg,
                    vertical = MindeckTheme.dimensions.spacingMd,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingMd),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(MindeckTheme.dimensions.spacingXxxl)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = deck.title.firstOrNull()?.toString() ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = deck.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Icon(
            painter = painterResource(Res.drawable.stat_minus_icon),
            contentDescription = stringResource(CreateCardRes.string.create_card_deck_clear),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(MindeckTheme.dimensions.iconSm),
        )
    }
}

@Composable
private fun PickPlaceholder(onPick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onPick)
                .padding(
                    horizontal = MindeckTheme.dimensions.spacingLg,
                    vertical = MindeckTheme.dimensions.spacingMd,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = MindeckTheme.shapes.avatar
                )
                    .padding(MindeckTheme.dimensions.spacingSm)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.style_icon),
                    contentDescription = stringResource(Res.string.action_add),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconXs)
                )
            }
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
            Text(
                text = stringResource(CreateCardRes.string.create_card_deck_create),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Icon(
            painter = painterResource(Res.drawable.stat_minus_icon),
            contentDescription = stringResource(CreateCardRes.string.create_card_deck_clear),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(MindeckTheme.dimensions.iconSm),
        )
    }
}
