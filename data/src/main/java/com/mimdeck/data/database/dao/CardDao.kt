package com.mimdeck.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mimdeck.data.database.entities.Card

interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Update
    suspend fun updateCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Query("UPDATE card SET deck_id = :targetDeckId WHERE card_id = :cardId")
    suspend fun moveCardToAnotherDeck(cardId: Int, targetDeckId: Int)
}