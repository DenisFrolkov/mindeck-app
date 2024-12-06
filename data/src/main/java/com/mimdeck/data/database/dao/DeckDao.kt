package com.mimdeck.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mimdeck.data.database.entities.Deck
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert()
    suspend fun insertDeck(deck: Deck)

    @Query("UPDATE deck SET deck_name = :newName WHERE folder_id = :deckId")
    suspend fun renameDeck(deckId: Int, newName: String)

    @Delete
    suspend fun deleteDeck(deck: Deck)

    @Query("SELECT * FROM deck WHERE deck_id = :folderId")
    fun getAllDecksByFolderId(folderId: Int): Flow<List<Deck>>

    @Query("DELETE FROM deck WHERE deck_id IN (:deckIds) AND folder_id = :folderId")
    suspend fun deleteDecksFromFolder(deckIds: List<Int>, folderId: Int)

    @Query("UPDATE deck SET folder_id = :folderId WHERE deck_id IN (:deckIds)")
    suspend fun addDecksToFolder(deckIds: List<Int>, folderId: Int)

    @Transaction
    suspend fun moveDecksBetweenFolders(deckIds: List<Int>, sourceFolderId: Int, targetFolderId: Int) {
        addDecksToFolder(deckIds, targetFolderId)
        deleteDecksFromFolder(deckIds, sourceFolderId)
    }
}