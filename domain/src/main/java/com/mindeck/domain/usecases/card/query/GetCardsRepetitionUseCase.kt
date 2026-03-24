package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.ClockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetCardsRepetitionUseCase @Inject constructor(
    private val cardRepetitionRepository: CardRepetitionRepository,
    private val clock: ClockRepository,
) {
    operator fun invoke(): Flow<List<Card>> {
        val now = clock.now()
        val todayStart = getTodayStartMillis(now)
        return cardRepetitionRepository.getCardsRepetition(currentTime = now, todayStart = todayStart)
            .map { cards -> buildSession(cards, now, todayStart) }
    }

    private fun buildSession(cards: List<Card>, now: Long, todayStart: Long): List<Card> {
        val learning = cards.filter { card ->
            (card.cardState == CardState.LEARNING || card.cardState == CardState.LAPSE) &&
                (card.nextReviewDate == null || card.nextReviewDate <= now)
        }

        val review = cards.filter { card ->
            card.cardState == CardState.REVIEW &&
                card.nextReviewDate != null && card.nextReviewDate <= now
        }

        val newShownToday = cards.count { card ->
            card.cardState != CardState.NEW &&
                card.firstReviewDate != null && card.firstReviewDate >= todayStart
        }
        val newLimit = (DAILY_NEW_LIMIT - newShownToday).coerceAtLeast(0)

        val new = cards
            .filter { it.cardState == CardState.NEW }
            .take(newLimit)

        return learning + review + new
    }

    private fun getTodayStartMillis(now: Long): Long {
        val millisInDay = TimeUnit.DAYS.toMillis(1)
        return (now / millisInDay) * millisInDay
    }

    companion object {
        private const val DAILY_NEW_LIMIT = 20
    }
}
