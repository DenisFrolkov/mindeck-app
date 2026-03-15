package com.mimdeck.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.CardWithDeckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(cardEntity: CardEntity)

    @Update
    suspend fun updateCard(cardEntity: CardEntity)

    @Query("DELETE FROM card WHERE card_id = :cardId")
    suspend fun deleteCard(cardId: Int)

    @Query("SELECT * FROM card WHERE deck_id = :deckId")
    fun getAllCardsByDeckId(deckId: Int): Flow<List<CardEntity>>

    @Query("SELECT * FROM card WHERE card_id = :cardId")
    fun getCardById(cardId: Int): Flow<CardEntity?>

    @Transaction
    @Query("SELECT * FROM card WHERE card_id = :cardId")
    fun getCardWithDeckById(cardId: Int): Flow<CardWithDeckEntity?>

    // Возвращает карточки для формирования сессии:
    // - все NEW (нужны для выдачи до дневного лимита)
    // - просроченные LEARNING/LAPSE/REVIEW (nextReviewDate <= currentTime)
    // - первый раз показанные сегодня (firstReviewDate >= todayStart, для подсчёта лимита)
    @Query(
        """
        SELECT * FROM card
        WHERE card_state = 'NEW'
           OR next_review_date <= :currentTime
           OR (first_review_date IS NOT NULL AND first_review_date >= :todayStart)
        ORDER BY card_id ASC
        """,
    )
    fun getCardsRepetition(currentTime: Long, todayStart: Long): Flow<List<CardEntity>>
}
