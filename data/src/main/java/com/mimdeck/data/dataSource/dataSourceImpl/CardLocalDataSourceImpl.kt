package com.mimdeck.data.dataSource.dataSourceImpl

import android.database.SQLException
import com.mimdeck.data.dataSource.CardDataSource
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.exception.DatabaseException
import kotlinx.coroutines.flow.Flow

class CardLocalDataSourceImpl (private val cardDao: CardDao) : CardDataSource {
    override suspend fun insertCard(cardEntity: CardEntity) {
        try {
            cardDao.insertCard(cardEntity = cardEntity)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to insert card due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error creating card: ${e.localizedMessage}", e)
        }
    }

    override suspend fun updateCard(cardEntity: CardEntity) {
        try {
            cardDao.updateCard(cardEntity = cardEntity)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to update card due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error updating card: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteCard(cardEntity: CardEntity) {
        try {
            cardDao.deleteCard(cardEntity = cardEntity)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to delete card due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error deleting card: ${e.localizedMessage}", e)
        }
    }

    override fun getAllCardsByDeckId(deckId: Int): Flow<List<CardEntity>> {
        return try {
            cardDao.getAllCardsByDeckId(deckId = deckId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to get all cards by deck is due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error creating card: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int) {
        try {
            cardDao.deleteCardsFromDeck(cardsIds = cardsIds, deckId = deckId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to delete cards from deck due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error deleting cards from deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int) {
        try {
            cardDao.addCardsToDeck(cardIds = cardIds, deckId = deckId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to add cards to deck due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error addition cards in deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun moveCardsBetweenDeck(cardIds: List<Int>, deckId: Int, targetDeckId: Int) {
        try {
            cardDao.moveCardsBetweenDeck(
                cardIds = cardIds,
                sourceDeckId = deckId,
                targetDeckId = targetDeckId
            )
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to move cards between decks due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error moving cards between decks: ${e.localizedMessage}", e)
        }
    }
}