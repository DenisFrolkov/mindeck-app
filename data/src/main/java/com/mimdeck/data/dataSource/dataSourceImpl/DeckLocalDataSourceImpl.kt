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

    override fun getAllDecks(): Flow<List<DeckEntity>> {
        return try {
            deckDao.getAllDecks()
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to get all decks by folder id due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error receiving decks by folder id: ${e.localizedMessage}", e)
        }
    }

    override suspend fun getDeckById(deckId: Int): DeckEntity {
        return try {
            deckDao.getDeckById(deckId = deckId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to get deck due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error getting a deck: ${e.localizedMessage}", e)
        }
    }
}