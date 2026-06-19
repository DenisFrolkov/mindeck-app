package com.mindeck.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.spacer.HorizontalSpacer
import com.mindeck.core.ui.spacer.VerticalSpacer
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.card.createCard.toSwatch
import com.mindeck.feature.card.model.DeckItem
import com.mindeck.feature.card.model.DeckPickState
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.action_add
import mindeck_app.core.ui.generated.resources.add_icon
import mindeck_app.core.ui.generated.resources.check_icon
import mindeck_app.core.ui.generated.resources.chevron_right_icon
import mindeck_app.core.ui.generated.resources.library_add_icon
import mindeck_app.core.ui.generated.resources.search_icon
import mindeck_app.core.ui.generated.resources.stat_minus_icon
import mindeck_app.core.ui.generated.resources.style_icon
import mindeck_app.feature.card.generated.resources.create_card_deck_clear
import mindeck_app.feature.card.generated.resources.create_card_deck_create
import mindeck_app.feature.card.generated.resources.create_card_deck_pick
import mindeck_app.feature.card.generated.resources.create_card_deck_search
import mindeck_app.feature.card.generated.resources.create_card_section_deck
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@Composable
internal fun DeckSection(
    state: DeckPickState,
    expanded: Boolean,
    onPickDeck: (Int) -> Unit,
    onClear: () -> Unit,
    onCreateDeck: () -> Unit,
    onExpand: () -> Unit,
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
            modifier =
                Modifier.fillMaxWidth().then(
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
                                                floatArrayOf(
                                                    dashLengthWidth.toPx(),
                                                    dashGapWidth.toPx(),
                                                ),
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
            colors =
                CardDefaults.cardColors(
                    if (!expanded) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainer,
                ),
        ) {
            when (state) {
                DeckPickState.NoDecks -> NoDecksContent(onCreateDeck = onCreateDeck)

                is DeckPickState.NotSelected ->
                    if (expanded) {
                        DeckPickerExpanded(
                            decks = state.decks,
                            selectedId = null,
                            onPickDeck = onPickDeck,
                            onCreateDeck = onCreateDeck,
                        )
                    } else {
                        PickCollapsed(onClick = onExpand)
                    }

                is DeckPickState.Selected ->
                    if (expanded) {
                        DeckPickerExpanded(
                            decks = state.decks,
                            selectedId = state.deck.id,
                            onPickDeck = onPickDeck,
                            onCreateDeck = onCreateDeck,
                        )
                    } else {
                        SelectedDeck(
                            deck = state.deck,
                            onClear = onClear,
                            onExpand = onExpand,
                        )
                    }
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
                modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MindeckTheme.shapes.avatar,
                        ).padding(MindeckTheme.dimensions.spacingSm),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.library_add_icon),
                    contentDescription = stringResource(Res.string.action_add),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconXs),
                )
            }
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
            Text(
                text = stringResource(CreateCardRes.string.create_card_deck_create),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
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
    onExpand: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onExpand)
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
            val swatch = deck.deckColor.toSwatch()
            Box(
                modifier =
                    Modifier
                        .size(MindeckTheme.dimensions.spacingXxxl)
                        .background(
                            color = swatch.container,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = deck.title.firstOrNull()?.toString() ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = swatch.onContainer,
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
                    .clickable(onClick = onClear)
                    .size(MindeckTheme.dimensions.iconSm),
        )
    }
}

@Composable
private fun PickCollapsed(onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
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
                modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = MindeckTheme.shapes.avatar,
                        ).padding(MindeckTheme.dimensions.spacingSm),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.style_icon),
                    contentDescription = stringResource(Res.string.action_add),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconXs),
                )
            }
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
            Text(
                text = stringResource(CreateCardRes.string.create_card_deck_pick),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Icon(
            painter = painterResource(Res.drawable.stat_minus_icon),
            contentDescription = stringResource(CreateCardRes.string.create_card_deck_pick),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(MindeckTheme.dimensions.iconSm),
        )
    }
}

@Composable
private fun DeckPickerExpanded(
    decks: List<DeckItem>,
    selectedId: Int?,
    onPickDeck: (Int) -> Unit,
    onCreateDeck: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val filtered =
        remember(decks, query) {
            if (query.isBlank()) {
                decks
            } else {
                decks.filter { it.title.contains(query.trim(), ignoreCase = true) }
            }
        }

    Column(
        modifier = Modifier.fillMaxWidth().padding(MindeckTheme.dimensions.spacingSm),
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
    ) {
        DeckSearchField(
            value = query,
            onValueChange = { query = it },
            count = decks.size,
        )

        Column {
            DeckList(
                decks = filtered,
                selectedId = selectedId,
                onPickDeck = onPickDeck,
            )
            HorizontalDivider(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .height(
                            MindeckTheme.dimensions.borderThin,
                        ),
            )
        }

        NewDeckRow(onClick = onCreateDeck)
    }
}

@Composable
private fun DeckList(
    decks: List<DeckItem>,
    selectedId: Int?,
    onPickDeck: (Int) -> Unit,
) {
    val spacing = MindeckTheme.dimensions.spacingXs
    val itemHeight = MindeckTheme.dimensions.iconLg + MindeckTheme.dimensions.spacingMd * 2
    val maxHeight = itemHeight * 3.5f + spacing * 2.5f

    LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(max = maxHeight),
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items(decks, key = { it.id }) { deck ->
            DeckListItem(
                deck = deck,
                pick = deck.id == selectedId,
                onPickDeck = onPickDeck,
            )
        }
    }
}

@Composable
private fun DeckSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    count: Int,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        decorationBox = { inner ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = MindeckTheme.shapes.avatar,
                        ).padding(
                            horizontal = MindeckTheme.dimensions.spacingLg,
                            vertical = MindeckTheme.dimensions.spacingMd,
                        ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.search_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconSm),
                )
                Row(Modifier.weight(1f)) {
                    if (value.isBlank()) {
                        HorizontalSpacer(width = MindeckTheme.dimensions.spacingSm)
                        Text(
                            text = stringResource(CreateCardRes.string.create_card_deck_search),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    inner()
                }
                Text(
                    count.toString(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
    )
}

@Composable
private fun NewDeckRow(onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(MindeckTheme.shapes.card)
                .clickable(onClick = onClick)
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
                modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MindeckTheme.shapes.avatar,
                        ).padding(MindeckTheme.dimensions.spacingSm),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_icon),
                    contentDescription = stringResource(CreateCardRes.string.create_card_deck_create),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconSm),
                )
            }
            HorizontalSpacer(MindeckTheme.dimensions.spacingSm)
            Text(
                text = stringResource(CreateCardRes.string.create_card_deck_create),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Icon(
            painter = painterResource(Res.drawable.chevron_right_icon),
            contentDescription = stringResource(CreateCardRes.string.create_card_deck_create),
            tint = MaterialTheme.colorScheme.outline,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(MindeckTheme.dimensions.iconSm),
        )
    }
}

@Composable
private fun DeckListItem(
    deck: DeckItem,
    pick: Boolean,
    onPickDeck: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = { onPickDeck(deck.id) }),
        shape = MindeckTheme.shapes.card,
        colors = CardDefaults.cardColors(if (pick) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(MindeckTheme.dimensions.spacingMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingMd),
            ) {
                val swatch = deck.deckColor.toSwatch()
                Box(
                    modifier =
                        Modifier
                            .size(MindeckTheme.dimensions.iconLg)
                            .background(
                                swatch.container,
                                shape = MaterialTheme.shapes.large,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = deck.title.firstOrNull()?.toString() ?: "",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = swatch.onContainer,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxs),
                ) {
                    Text(
                        text = deck.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            if (pick) {
                Icon(
                    painter = painterResource(Res.drawable.check_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconSm),
                )
            }
        }
    }
}
