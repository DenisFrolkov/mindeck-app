package com.mindeck.feature.home

import com.mindeck.domain.models.DeckWithStats
import com.mindeck.feature.home.model.DeckItem

fun DeckWithStats.toUi() =
    DeckItem(
        id = deck.deckId,
        title = deck.deckName,
        deckColor = deck.deckColor,
        cardCount = cardCount,
        newCount = newCount,
        newReviewCount = newReviewCount,
        reviewCount = reviewCount,
    )
