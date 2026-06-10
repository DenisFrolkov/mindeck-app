package com.mindeck.feature.home.home

import com.mindeck.domain.models.DeckColor

data class DeckItem(
    val id: Int,
    val title: String,
    val deckColor: DeckColor,
    val cardCount: Int,
    val newCount: Int,
    val newReviewCount: Int,
    val reviewCount: Int,
) {
    val hasCardsToReview: Boolean get() = newCount + newReviewCount + reviewCount > 0
}