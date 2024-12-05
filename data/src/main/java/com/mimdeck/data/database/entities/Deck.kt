package com.mimdeck.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.SET_DEFAULT
import androidx.room.Index

@Entity(
    tableName = "decks",
    indices = [Index(value = ["deck_name"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = ["folderId"],
        childColumns = ["folder_id"],
        onDelete = SET_DEFAULT
    )]
)
data class Deck(
    @PrimaryKey
    @ColumnInfo(name = "deck_id")
    val deckId: Int,
    @ColumnInfo(name = "deck_name") val deckName: String,
    @ColumnInfo(name = "folder_id") val folderId: Int = 1,
    @ColumnInfo(name = "creation_date") val creationDate: Long
)