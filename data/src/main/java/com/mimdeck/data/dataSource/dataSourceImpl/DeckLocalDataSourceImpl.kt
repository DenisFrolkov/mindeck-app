package com.mimdeck.data.dataSource.dataSourceImpl

import android.database.SQLException
import com.mimdeck.data.dataSource.DeckDataSource
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.entities.DeckEntity
import com.mimdeck.data.exception.DatabaseException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeckLocalDataSourceImpl @Inject constructor(private val deckDao: DeckDao) : DeckDataSource {
    override suspend fun insertDeck(deckEntity: DeckEntity) {
        try {
            deckDao.insertDeck(deckEntity = deckEntity)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to insert deck due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error creating deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun renameDeck(deckId: Int, newName: String) {
        try {
            deckDao.renameDeck(deckId = deckId, newName = newName)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to rename deck due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error renaming deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteDeck(deckEntity: DeckEntity) {
        try {
            deckDao.deleteDeck(deckEntity = deckEntity)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to delete deck due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error deleting folder: ${e.localizedMessage}", e)
        }
    }

    override fun getAllDecksByFolderId(folderId: Int): Flow<List<DeckEntity>> {
        return try {
            deckDao.getAllDecksByFolderId(folderId = folderId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to get all decks by folder id due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error receiving decks by folder id: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteDecksFromFolder(deckIds: List<Int>, folderId: Int) {
        try {
            deckDao.deleteDecksFromFolder(deckIds = deckIds, folderId = folderId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to delete decks from folder due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error deleting decks from folder: ${e.localizedMessage}", e)
        }
    }

    override suspend fun addDecksToFolder(deckIds: List<Int>, folderId: Int) {
        try {
            deckDao.addDecksToFolder(deckIds = deckIds, folderId = folderId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to add decks to folder due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error addition decks in folder: ${e.localizedMessage}", e)
        }
    }

    override suspend fun moveDecksBetweenFolders(
        deckIds: List<Int>,
        sourceFolderId: Int,
        targetFolderId: Int
    ) {
        try {
            deckDao.moveDecksBetweenFolders(
                deckIds = deckIds,
                sourceFolderId = sourceFolderId,
                targetFolderId = targetFolderId
            )
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to move decks between folders due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error moving decks between folders: ${e.localizedMessage}", e)
        }
    }
}