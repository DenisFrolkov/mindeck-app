package com.mindeck.domain.repository

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import kotlinx.coroutines.flow.Flow

interface CardRepetitionRepository {
    fun getCardsRepetition(currentTime: Long): Flow<List<Card>>

    suspend fun updateReview(
        cardId: Int,
        firstReviewDate: Long,
        lastReviewDate: Long,
        newReviewDate: Long,
        newRepetitionCount: Int,
        lastReviewType: ReviewType,
    )
}
