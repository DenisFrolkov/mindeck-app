package com.mindeck.data.model

data class DeckCardStats(
    val deckId: Int,
    val cardCount: Int,
    val newCount: Int,
    val newReviewCount: Int,
    val reviewCount: Int,
)