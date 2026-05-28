package com.mindeck.domain.repository

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardWithDeck
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun insertCard(card: Card)
    suspend fun updateCard(card: Card)
    suspend fun deleteCard(cardId: Int)
    fun getAllCardsByDeckId(deckId: Int): Flow<List<Card>>
    fun getCardById(cardId: Int): Flow<Card?>
    fun getCardWithDeckById(cardId: Int): Flow<CardWithDeck?>
}
