package com.mimdeck.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deck",
    indices = [Index(
        value = ["deck_name"],
        unique = true
    ), Index(value = ["deck_id"])]
)
data class DeckEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "deck_id")
    val deckId: Int = 0,
    @ColumnInfo(name = "deck_name") val deckName: String?
)