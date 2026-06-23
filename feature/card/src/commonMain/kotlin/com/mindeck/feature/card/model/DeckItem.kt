package com.mindeck.feature.card.model

import com.mindeck.domain.models.DeckColor

data class DeckItem(
    val id: Int,
    val title: String,
    val deckColor: DeckColor,
)
