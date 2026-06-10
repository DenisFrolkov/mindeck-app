package com.mindeck.data.models

data class DeckCardStats(
    val deckId: Int,
    val cardCount: Int,
    val newCount: Int,
    val newReviewCount: Int,
    val reviewCount: Int,
)
