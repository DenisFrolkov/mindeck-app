package com.mindeck.domain.repository

import com.mindeck.domain.models.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    suspend fun insertDeck(deck: Deck)

    suspend fun renameDeck(deckId: Int, newName: String)

    suspend fun deleteDeck(deck: Deck)

    fun getAllDecksByFolderId(folderId: Int): Flow<List<Deck>>

    suspend fun deleteDecksFromFolder(deckIds: List<Int>, folderId: Int)

    suspend fun addDecksToFolder(deckIds: List<Int>, folderId: Int)

    suspend fun moveDecksBetweenFolders(deckIds: List<Int>, sourceFolderId: Int, targetFolderId: Int) {
        addDecksToFolder(deckIds, targetFolderId)
        deleteDecksFromFolder(deckIds, sourceFolderId)
    }
}