package com.mimdeck.data.repository

import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.repository.util.handleDatabaseSuspend
import com.mindeck.domain.repository.CardDeckOperations
import javax.inject.Inject

class CardDeckOperationsImpl @Inject constructor(
    private val cardDao: CardDao
) : CardDeckOperations {
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
}