package com.mindeck.feature.home.model

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
    val dueCount: Int get() = newCount + newReviewCount + reviewCount

    val hasCardsToReview: Boolean get() = dueCount > 0
}
