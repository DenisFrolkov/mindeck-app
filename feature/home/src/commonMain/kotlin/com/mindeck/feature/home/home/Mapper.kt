package com.mindeck.feature.home.home

import com.mindeck.domain.models.DeckWithStats

fun DeckWithStats.toUi() = DeckItem(
    id = deck.deckId,
    title = deck.deckName,
    deckColor = deck.deckColor,
    cardCount = cardCount,
    newCount = newCount,
    newReviewCount = newReviewCount,
    reviewCount = reviewCount,
)