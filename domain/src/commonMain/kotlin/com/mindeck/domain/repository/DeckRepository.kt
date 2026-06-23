package com.mindeck.domain.repository

import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.DeckWithStats
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    suspend fun insertDeck(deck: Deck): Int

    suspend fun renameDeck(
        deckId: Int,
        newName: String,
    )

    suspend fun deleteDeck(deckId: Int)

    fun getAllDecks(): Flow<List<Deck>>

    fun getDeckById(deckId: Int): Flow<Deck?>

    fun getCardStatsPerDeck(currentTime: Long): Flow<List<DeckWithStats>>
}
