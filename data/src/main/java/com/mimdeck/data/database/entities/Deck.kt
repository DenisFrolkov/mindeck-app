package com.mimdeck.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "decks",
    indices = [Index(value = ["deck_name"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = ["folderId"],
        childColumns = ["folder_id"],
        onDelete = ForeignKey.SET_DEFAULT
    )]
)
data class Deck(
    @PrimaryKey
    @ColumnInfo(name = "deck_id")
    val deckId: Int,
    @ColumnInfo(name = "deck_name") val deckName: String,
    @ColumnInfo(name = "folder_id") val folderId: Int,
)