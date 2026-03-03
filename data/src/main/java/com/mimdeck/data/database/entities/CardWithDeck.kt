package com.mimdeck.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CardWithDeck(
    @Embedded val card: CardEntity,
    @Relation(
        parentColumn = "deck_id",
        entityColumn = "deck_id",
    )
    val deck: DeckEntity,
)
