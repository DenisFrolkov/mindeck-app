package com.mimdeck.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "cards", indices = [Index(value = ["card_name", "card_question"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Deck::class,
        parentColumns = ["deckId"],
        childColumns = ["deck_id"],
        onDelete = ForeignKey.SET_DEFAULT
    )]
)
data class Card(
    @PrimaryKey
    @ColumnInfo(name = "card_id")
    val cardId: Int,
    @ColumnInfo(name = "card_name") val cardName: String,
    @ColumnInfo(name = "card_question") val cardQuestion: String,
    @ColumnInfo(name = "card_answer") val cardAnswer: String,
    @ColumnInfo(name = "card_folder") val cardFolder: List<String>,
    @ColumnInfo(name = "card_deck") val cardDeck: List<String>,
    @ColumnInfo(name = "card_priority") val cardPriority: String,
    @ColumnInfo(name = "card_type") val cardType: String,
    @ColumnInfo(name = "card_tag") val cardTag: String,
    @ColumnInfo(name = "deck_id") val deckId: Int
)
