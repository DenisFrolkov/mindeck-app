package com.mimdeck.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mimdeck.data.database.entities.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert()
    suspend fun insertCard(cardEntity: CardEntity)

    @Update
    suspend fun updateCard(cardEntity: CardEntity)

    @Delete
    suspend fun deleteCard(cardEntity: CardEntity)

    @Query("SELECT * FROM card WHERE deck_id = :deckId")
    fun getAllCardsByDeckId(deckId: Int): Flow<List<CardEntity>>

    @Query("DELETE FROM card WHERE deck_id IN (:cardsIds) AND deck_id = :deckId")
    suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int)

    @Query("UPDATE card SET deck_id = :deckId WHERE card_id IN (:cardIds)")
    suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int)

    @Transaction
    suspend fun moveCardsBetweenDeck(cardIds: List<Int>, sourceDeckId: Int, targetDeckId: Int) {
        addCardsToDeck(cardIds, targetDeckId)
        deleteCardsFromDeck(cardIds, sourceDeckId)
    }
}