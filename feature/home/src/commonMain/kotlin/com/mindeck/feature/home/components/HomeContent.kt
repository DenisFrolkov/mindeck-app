package com.mindeck.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindeck.core.ui.theme.MindeckTheme
import com.mindeck.feature.home.HomeUiState

@Composable
internal fun HomeContent(
    content: HomeUiState.Content,
    onReview: () -> Unit,
    onViewStatistics: () -> Unit,
    onAllDecks: () -> Unit,
    onOpenDeck: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.spacingXxxl),
    ) {
        DailyReviewCard(
            dailyReview = content.dailyReview,
            onReview = onReview,
            onViewStatistics = onViewStatistics,
            modifier = Modifier.padding(top = MindeckTheme.dimensions.spacingMd),
        )
        DeckList(
            decks = content.decks,
            onAllDecks = onAllDecks,
            onOpenDeck = onOpenDeck,
            modifier = Modifier.weight(1f),
        )
    }
}
