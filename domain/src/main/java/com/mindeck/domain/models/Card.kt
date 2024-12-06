package com.mindeck.domain.models

data class Card(
    val cardId: Int,
    val cardName: String,
    val cardQuestion: String,
    val cardAnswer: String,
    val cardPriority: String,
    val cardType: String,
    val cardTag: String,
    val deckId: Int,
)
