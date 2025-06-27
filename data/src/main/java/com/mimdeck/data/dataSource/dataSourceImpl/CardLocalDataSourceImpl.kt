package com.mimdeck.data.dataSource.dataSourceImpl

import android.database.SQLException
import android.util.Log
import com.mimdeck.data.dataSource.CardDataSource
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.exception.DatabaseException
import com.mindeck.domain.models.ReviewType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardLocalDataSourceImpl @Inject constructor(private val cardDao: CardDao) : CardDataSource {
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

    override suspend fun getCardById(cardId: Int): CardEntity {
        return try {
            cardDao.getCardById(cardId = cardId)
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to get card due to a constraint violation: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException("Error getting a card: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int) {
        try {
            cardDao.deleteCardsFromDeck(cardIds = cardsIds, deckId = deckId)
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

    override suspend fun moveCardsBetweenDeck(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        try {
            cardDao.moveCardsBetweenDeck(
                cardIds = cardIds,
                sourceDeckId = sourceDeckId,
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

    override fun getCardsRepetition(currentTime: Long): Flow<List<CardEntity>> {
        try {
            val card = cardDao.getCardsRepetition(currentTime = currentTime)
            Log.d("CardLocalDataSourceImpl", "Cards for repetition: $card")
            return card

        } catch (e: SQLException) {
            throw DatabaseException("Failed to get repetition cards: ${e.localizedMessage}", e)
        } catch (e: Exception) {
            throw DatabaseException("Error getting repetition cards: ${e.localizedMessage}", e)
        }
    }

    override suspend fun updateReview(
        cardId: Int,
        firstReviewDate: Long,
        lastReviewDate: Long,
        newReviewDate: Long,
        newRepetitionCount: Int,
        lastReviewType: ReviewType
    ) {
        try {
            cardDao.updateReview(
                cardId,
                lastReviewDate,
                firstReviewDate,
                newReviewDate,
                newRepetitionCount,
                lastReviewType
            )
        } catch (e: SQLException) {
            throw DatabaseException(
                "Failed to update data for repeating the card: ${e.localizedMessage}",
                e
            )
        } catch (e: Exception) {
            throw DatabaseException(
                "Error updating review data for the card: ${e.localizedMessage}",
                e
            )
        }
    }
}