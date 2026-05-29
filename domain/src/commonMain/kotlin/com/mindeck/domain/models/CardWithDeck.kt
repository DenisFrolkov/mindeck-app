package com.mindeck.domain.models

data class CardWithDeck(
    val card: Card,
    val deckId: Int,
    val deckName: String,
)
