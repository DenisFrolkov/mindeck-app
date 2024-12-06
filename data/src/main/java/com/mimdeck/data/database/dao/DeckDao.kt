package com.mimdeck.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mimdeck.data.database.entities.Deck

interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: Deck)

    @Query("UPDATE deck SET deck_name = :newName WHERE folder_id = :deckId")
    suspend fun renameDeck(deckId: Int, newName: String)

    @Delete
    suspend fun deleteDeck(deck: Deck)

    @Query("SELECT * FROM card WHERE deck_id = :deckId")
    fun getAllDecksByFolderId(deckId: Int): List<Deck>

    @Query("DELETE FROM card WHERE deck_id IN (:cardsIds) AND deck_id = :deckId")
    suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int)

    @Query("UPDATE card SET deck_id = :deckId WHERE card_id IN (:cardIds)")
    suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int)

    @Transaction
    suspend fun moveDecksBetweenFolders(deckIds: List<Int>, sourceFolderId: Int, targetFolderId: Int) {
        addCardsToDeck(deckIds, targetFolderId)
        deleteCardsFromDeck(deckIds, sourceFolderId)
    }
}