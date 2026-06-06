package com.mindeck.feature.home.home

data class DeckUi(
    val id: Int,
    val title: String,
    val cardCount: Int,
    val reviewCount: Int,
) {
    val hasCardsToReview: Boolean get() = reviewCount > 0
}
