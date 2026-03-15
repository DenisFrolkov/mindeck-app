package com.mindeck.domain.repository

import com.mindeck.domain.models.Card
import kotlinx.coroutines.flow.Flow

interface CardRepetitionRepository {
    // Возвращает карточки для сессии:
    // - NEW (для показа до дневного лимита)
    // - просроченные LEARNING/LAPSE/REVIEW (nextReviewDate <= currentTime)
    // - первый раз показанные сегодня (firstReviewDate >= todayStart, для подсчёта лимита)
    fun getCardsRepetition(currentTime: Long, todayStart: Long): Flow<List<Card>>

    // Сохраняет обновлённое состояние карточки после оценки пользователем
    suspend fun updateReview(card: Card)
}
