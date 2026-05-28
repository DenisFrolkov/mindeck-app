package com.mindeck.domain.repository

import com.mindeck.domain.models.Card
import kotlinx.coroutines.flow.Flow

interface CardRepetitionRepository {
    fun getCardsRepetition(currentTime: Long, todayStart: Long): Flow<List<Card>>

    suspend fun updateReview(card: Card)
}
