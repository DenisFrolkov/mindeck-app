package com.mindeck.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mindeck.data.entities.DeckEntity
import com.mindeck.data.model.DeckCardStats
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
    suspend fun insertDeck(deckEntity: DeckEntity): Long

    @Query("UPDATE deck SET deck_name = :newName WHERE deck_id = :deckId")
    suspend fun renameDeck(
        deckId: Int,
        newName: String,
    )

    @Query("DELETE FROM deck WHERE deck_id = :deckId")
    suspend fun deleteDeck(deckId: Int)

    @Query("SELECT * FROM deck")
    fun getAllDecks(): Flow<List<DeckEntity>>

    @Query("SELECT * FROM deck WHERE deck_id = :deckId")
    fun getDeckById(deckId: Int): Flow<DeckEntity?>

    @Query("""
      SELECT deck_id AS deckId,
          COUNT(*) AS cardCount,
          SUM(CASE WHEN card_state = 'NEW' THEN 1 ELSE 0 END) AS newCount,
          SUM(CASE WHEN card_state IN ('LEARNING','LAPSE')
                   AND (next_review_date IS NULL OR next_review_date <= :currentTime)
                   THEN 1 ELSE 0 END) AS newReviewCount,
          SUM(CASE WHEN card_state = 'REVIEW'
                   AND next_review_date IS NOT NULL
                   AND next_review_date <= :currentTime
                   THEN 1 ELSE 0 END) AS reviewCount
      FROM card
      GROUP BY deck_id
    """)
    fun getReviewCountPerDeck(currentTime: Long): Flow<List<DeckCardStats>>
}
