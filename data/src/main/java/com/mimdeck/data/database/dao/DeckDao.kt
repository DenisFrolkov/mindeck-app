package com.mimdeck.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mimdeck.data.database.entities.DeckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
    suspend fun insertDeck(deckEntity: DeckEntity): Long

    @Query("UPDATE deck SET deck_name = :newName WHERE deck_id = :deckId")
    suspend fun renameDeck(deckId: Int, newName: String)

    @Query("DELETE FROM deck WHERE deck_id = :deckId")
    suspend fun deleteDeck(deckId: Int)

    @Query("SELECT * FROM deck")
    fun getAllDecks(): Flow<List<DeckEntity>>

    @Query("SELECT * FROM deck WHERE deck_id = :deckId")
    fun getDeckById(deckId: Int): Flow<DeckEntity?>
}
