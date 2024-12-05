package com.mimdeck.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class FolderWithDecks(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folder_id",
        entityColumn = "folder_id"
    )
    val decks: List<Deck>
)
