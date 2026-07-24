package com.mindeck.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.mindeck.core.ui.format.deckCoverInitial
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.home.model.DeckItem
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.check_circle_icon
import mindeck_app.core.ui.generated.resources.library_decks_icon
import mindeck_app.feature.home.generated.resources.home_deck_all_reviewed
import mindeck_app.feature.home.generated.resources.home_deck_card_count
import mindeck_app.feature.home.generated.resources.home_decks_all
import mindeck_app.feature.home.generated.resources.home_decks_header
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.home.generated.resources.Res as HomeRes

@Composable
internal fun DeckList(
    decks: List<DeckItem>,
    onAllDecks: () -> Unit,
    onOpenDeck: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingLg),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                stringResource(HomeRes.string.home_decks_header),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = MindeckTheme.dimensions.spacingSm),
            )
            Row(
                modifier =
                    Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable(onClick = onAllDecks)
                        .padding(end = MindeckTheme.dimensions.spacingLg),
                horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.library_decks_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(MindeckTheme.dimensions.iconXs),
                )
                Text(
                    text = stringResource(HomeRes.string.home_decks_all),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingSm),
        ) {
            items(decks, key = { it.id }) { deck ->
                DeckListItem(deck = deck, onOpenDeck = onOpenDeck)
            }
        }
    }
}

@Composable
private fun DeckListItem(
    deck: DeckItem,
    onOpenDeck: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = { onOpenDeck(deck.id) }),
        shape = MindeckTheme.shapes.card,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)),
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
                            .size(MindeckTheme.dimensions.touchTarget)
                            .background(
                                swatch.container,
                                shape = MaterialTheme.shapes.medium,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = deckCoverInitial(deck.title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = swatch.onContainer,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxs),
                ) {
                    Text(
                        text = deck.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text =
                            pluralStringResource(
                                HomeRes.plurals.home_deck_card_count,
                                deck.cardCount,
                                deck.cardCount,
                            ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (deck.hasCardsToReview) {
                Box(
                    modifier =
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium,
                            ).size(
                                height = MindeckTheme.dimensions.spacingXxl,
                                width = MindeckTheme.dimensions.touchTarget,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = deck.dueCount.toString(),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            } else {
                Icon(
                    painter = painterResource(Res.drawable.check_circle_icon),
                    contentDescription = stringResource(HomeRes.string.home_deck_all_reviewed),
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}
