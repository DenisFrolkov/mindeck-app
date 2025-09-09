package com.mimdeck.data.repository

import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.mapper.Mappers.toData
import com.mimdeck.data.database.mapper.Mappers.toDomain
import com.mimdeck.data.util.handleDatabase
import com.mimdeck.data.util.handleDatabaseSuspend
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(private val cardDao: CardDao) : CardRepository {
    override suspend fun insertCard(card: Card) = handleDatabaseSuspend {
        cardDao.insertCard(cardEntity = card.toData())
    }

    override suspend fun updateCard(card: Card) = handleDatabaseSuspend {
        cardDao.updateCard(cardEntity = card.toData())
    }

    override suspend fun deleteCard(card: Card) = handleDatabaseSuspend {
        cardDao.deleteCard(cardEntity = card.toData())
    }

    override fun getAllCardsByDeckId(deckId: Int): Flow<List<Card>> = handleDatabase {
        cardDao.getAllCardsByDeckId(deckId = deckId)
            .map { cardsEntityList -> cardsEntityList.map { it.toDomain() } }
    }

    override suspend fun getCardById(cardId: Int): Card = handleDatabaseSuspend {
        cardDao.getCardById(cardId = cardId).toDomain()
    }

    override suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int) = handleDatabaseSuspend {
        cardDao.deleteCardsFromDeck(cardIds = cardsIds, deckId = deckId)
    }

    override suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int) = handleDatabaseSuspend {
        cardDao.addCardsToDeck(cardIds = cardIds, deckId = deckId)
    }

    override suspend fun moveCardsBetweenDeck(
        cardIds: List<Int>,
        sourceDeckId: Int,
        targetDeckId: Int
    ) = handleDatabaseSuspend {
        cardDao.moveCardsBetweenDeck(
            cardIds = cardIds,
            sourceDeckId = sourceDeckId,
            targetDeckId = targetDeckId
        )
    }

    override fun getCardsRepetition(currentTime: Long): Flow<List<Card>> = handleDatabase {
        cardDao.getCardsRepetition(currentTime = currentTime)
            .map { cardsEntityList -> cardsEntityList.map { it.toDomain() } }
    }

    override suspend fun updateReview(
        cardId: Int,
        firstReviewDate: Long,
        lastReviewDate: Long,
        newReviewDate: Long,
        newRepetitionCount: Int,
        lastReviewType: ReviewType
    ) = handleDatabaseSuspend {
        cardDao.updateReview(
            cardId,
            firstReviewDate,
            lastReviewDate,
            newReviewDate,
            newRepetitionCount,
            lastReviewType
        )
    }
}