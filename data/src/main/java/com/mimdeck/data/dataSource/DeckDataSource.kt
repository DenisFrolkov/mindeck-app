package com.mimdeck.data.dataSource

import com.mimdeck.data.database.entities.DeckEntity
import kotlinx.coroutines.flow.Flow

interface DeckDataSource {
    suspend fun insertDeck(deckEntity: DeckEntity)

    suspend fun renameDeck(deckId: Int, newName: String)

    suspend fun deleteDeck(deckEntity: DeckEntity)

    fun getAllDecks(): Flow<List<DeckEntity>>

    suspend fun getDeckById(deckId: Int): DeckEntity
}