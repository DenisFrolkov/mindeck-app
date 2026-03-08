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

    @Query("SELECT * FROM card WHERE next_review_date IS NULL OR next_review_date <= :currentTime")
    fun getCardsRepetition(currentTime: Long): Flow<List<CardEntity>>

    @Query(
        """
        UPDATE card
        SET first_review_date = :firstReviewDate,
            last_review_date = :lastReviewDate,
            next_review_date = :newReviewDate,
            repetition_count = :newRepetitionCount,
            last_review_type = :lastReviewType
        WHERE card_id = :cardId
    """,
    )
    suspend fun updateReview(
        cardId: Int,
        firstReviewDate: Long,
        lastReviewDate: Long,
        newReviewDate: Long,
        newRepetitionCount: Int,
        lastReviewType: String,
    )
}
