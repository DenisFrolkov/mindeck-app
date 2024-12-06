package com.mimdeck.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mimdeck.data.database.entities.Deck
import com.mimdeck.data.database.entities.Folder
import kotlinx.coroutines.flow.Flow

interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder)

    @Query("UPDATE folder SET folder_name = :newName WHERE folder_id = :folderId")
    suspend fun renameFolder(folderId: Int, newName: String)

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Query("SELECT * FROM folder")
    suspend fun getAllFolders(): List<Folder>

    @Query("SELECT * FROM deck WHERE folder_id = :folderId")
    fun getAllDecksByFolderId(folderId: Int): List<Deck>

    @Query("DELETE FROM deck WHERE deck_id IN (:deckIds) AND folder_id = :folderId")
    suspend fun deleteDecksFromFolder(deckIds: List<Int>, folderId: Int)

    @Query("UPDATE deck SET folder_id = :folderId WHERE deck_id IN (:deckIds)")
    suspend fun addDecksToFolder(deckIds: List<Int>, folderId: Int)

    @Transaction
    suspend fun moveDecksBetweenFolder(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        deleteDecksFromFolder(cardIds, sourceDeckId)
        addDecksToFolder(cardIds, targetDeckId)
    }
}