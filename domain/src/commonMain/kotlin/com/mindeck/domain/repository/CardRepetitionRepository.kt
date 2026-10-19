package com.mindeck.domain.repository

import com.mindeck.domain.models.Card

interface CardRepetitionRepository {
    suspend fun getCardsRepetition(
        currentTime: Long,
        todayStart: Long,
    ): List<Card>

    suspend fun getLearnAheadCard(until: Long): Card?

    suspend fun updateReview(card: Card)
}
