package com.mimdeck.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.SET_DEFAULT
import androidx.room.Index

@Entity(
    tableName = "cards", indices = [Index(value = ["card_name", "card_question"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = ["folderId"],
        childColumns = ["folder_id"],
        onDelete = SET_DEFAULT
    ), ForeignKey(
        entity = Deck::class,
        parentColumns = ["deckId"],
        childColumns = ["deck_id"],
        onDelete = SET_DEFAULT
    )]
)
data class Card(
    @PrimaryKey
    @ColumnInfo(name = "card_id")
    val cardId: Int,
    @ColumnInfo(name = "card_name") val cardName: String,
    @ColumnInfo(name = "card_question") val cardQuestion: String,
    @ColumnInfo(name = "card_answer") val cardAnswer: String,
    @ColumnInfo(name = "card_priority") val cardPriority: String,
    @ColumnInfo(name = "card_type") val cardType: String,
    @ColumnInfo(name = "card_tag") val cardTag: String,
    @ColumnInfo(name = "folder_id") val folderId: Int = 1,
    @ColumnInfo(name = "deck_id") val deckId: Int = 1,
    @ColumnInfo(name = "creation_date") val creationDate: Long
)
