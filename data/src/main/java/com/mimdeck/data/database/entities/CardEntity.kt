package com.mimdeck.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "card", indices = [Index(value = ["card_name", "card_question"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = DeckEntity::class,
        parentColumns = ["deck_id"],
        childColumns = ["deck_id"],
        onDelete = CASCADE
    )]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id")
    val cardId: Int = 0,
    @ColumnInfo(name = "card_name") val cardName: String?,
    @ColumnInfo(name = "card_question") val cardQuestion: String?,
    @ColumnInfo(name = "card_answer") val cardAnswer: String,
    @ColumnInfo(name = "card_type") val cardType: String,
    @ColumnInfo(name = "card_tag") val cardTag: String,
    @ColumnInfo(name = "deck_id") val deckId: Int,
)
