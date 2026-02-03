package com.mimdeck.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mimdeck.data.database.entities.DeckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert()
    suspend fun insertDeck(deckEntity: DeckEntity)

    @Query("UPDATE deck SET deck_name = :newName WHERE deck_id = :deckId")
    suspend fun renameDeck(deckId: Int, newName: String)

    @Delete
    suspend fun deleteDeck(deckEntity: DeckEntity)

    @Query("SELECT * FROM deck")
    fun getAllDecks(): Flow<List<DeckEntity>>

    @Query("SELECT * FROM deck WHERE deck_id = :deckId")
    suspend fun getDeckById(deckId: Int): DeckEntity
}
