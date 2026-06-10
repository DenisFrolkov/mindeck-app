package com.mindeck.domain.models

data class DeckWithStats(
    val deck: Deck,
    val cardCount: Int,
    val newCount: Int,
    val newReviewCount: Int,
    val reviewCount: Int,
)
