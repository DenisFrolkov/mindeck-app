package com.mindeck.domain.repository

import com.mindeck.domain.models.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun insertCard(card: Card)

    suspend fun updateCard(card: Card)

    suspend fun deleteCard(card: Card)

    fun getAllCardsByDeckId(deckId: Int): Flow<List<Card>>

    suspend fun getCardById(cardId: Int): Card

    suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int)

    suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int)

    suspend fun moveCardsBetweenDeck(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        addCardsToDeck(cardIds, targetDeckId)
        deleteCardsFromDeck(cardIds, sourceDeckId)
    }
}