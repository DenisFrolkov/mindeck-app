package com.mimdeck.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class DeckWithCards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "deck_id",
        entityColumn = "deck_id"
    )
    val cards: List<Card>
)