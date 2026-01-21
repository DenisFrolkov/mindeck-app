package com.mindeck.domain.repository

import com.mindeck.domain.models.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    suspend fun insertDeck(deck: Deck)

    suspend fun renameDeck(deckId: Int, newName: String)

    suspend fun deleteDeck(deck: Deck)

    fun getAllDecks(): Flow<List<Deck>>

    suspend fun getDeckById(deckId: Int): Deck
}
