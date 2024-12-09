package com.mimdeck.data.dataSource

import com.mimdeck.data.database.entities.DeckEntity
import kotlinx.coroutines.flow.Flow

interface DeckDataSource {
    suspend fun insertDeck(deckEntity: DeckEntity)

    suspend fun renameDeck(deckId: Int, newName: String)

    suspend fun deleteDeck(deckEntity: DeckEntity)

    fun getAllDecksByFolderId(folderId: Int): Flow<List<DeckEntity>>

    suspend fun deleteDecksFromFolder(deckIds: List<Int>, folderId: Int)

    suspend fun addDecksToFolder(deckIds: List<Int>, folderId: Int)

    suspend fun moveDecksBetweenFolders(
        deckIds: List<Int>,
        sourceFolderId: Int,
        targetFolderId: Int
    ) {
        addDecksToFolder(deckIds, targetFolderId)
        deleteDecksFromFolder(deckIds, sourceFolderId)
    }
}