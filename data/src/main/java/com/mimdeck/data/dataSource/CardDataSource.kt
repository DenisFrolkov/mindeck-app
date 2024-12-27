package com.mimdeck.data.dataSource

import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.FolderEntity
import kotlinx.coroutines.flow.Flow

interface CardDataSource {
    suspend fun insertCard(cardEntity: CardEntity)

    suspend fun updateCard(cardEntity: CardEntity)

    suspend fun deleteCard(cardEntity: CardEntity)

    fun getAllCardsByDeckId(deckId: Int): Flow<List<CardEntity>>

    suspend fun getCardById(cardId: Int): CardEntity

    suspend fun getFolderByCardId(cardId: Int): FolderEntity?

    suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int)

    suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int)

    suspend fun moveCardsBetweenDeck(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        addCardsToDeck(cardIds, targetDeckId)
        deleteCardsFromDeck(cardIds, sourceDeckId)
    }
}