package com.mindeck.domain.repository

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import kotlinx.coroutines.flow.Flow

interface CardRepetitionRepository {
    // Возвращает карточки для сессии:
    // - NEW (для показа до дневного лимита)
    // - просроченные LEARNING/LAPSE/REVIEW (nextReviewDate <= currentTime)
    // - первый раз показанные сегодня (firstReviewDate >= todayStart, для подсчёта лимита)
    fun getCardsRepetition(currentTime: Long, todayStart: Long): Flow<List<Card>>

    // Сохраняет обновлённое состояние карточки после оценки пользователем
    suspend fun updateReview(
        cardId: Int,
        cardState: CardState,
        easeFactor: Float,
        interval: Float,
        learningStep: Int,
        nextReviewDate: Long,
        repetitionCount: Int,
        lapseCount: Int,
        firstReviewDate: Long,
        lastReviewDate: Long,
    )
}
