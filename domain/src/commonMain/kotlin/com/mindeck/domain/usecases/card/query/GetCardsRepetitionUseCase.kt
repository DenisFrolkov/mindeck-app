package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.time.TimeProvider
import com.mindeck.domain.time.startOfDayMillis

class GetCardsRepetitionUseCase(
    private val cardRepetitionRepository: CardRepetitionRepository,
    private val clock: TimeProvider,
) {
    suspend operator fun invoke(): List<Card> {
        val now = clock.now()
        val todayStart = startOfDayMillis(now, clock.timeZone())
        val cards =
            cardRepetitionRepository.getCardsRepetition(currentTime = now, todayStart = todayStart)
        return buildSession(cards, now, todayStart).ifEmpty { learnAhead(now) }
    }

    private suspend fun learnAhead(now: Long): List<Card> =
        listOfNotNull(cardRepetitionRepository.getLearnAheadCard(now + LEARN_AHEAD_MILLIS))

    private fun buildSession(
        cards: List<Card>,
        now: Long,
        todayStart: Long,
    ): List<Card> {
        val learning =
            cards.filter { card ->
                (card.cardState == CardState.LEARNING || card.cardState == CardState.LAPSE) &&
                    (card.nextReviewDate == null || card.nextReviewDate <= now)
            }

        val review =
            cards.filter { card ->
                card.cardState == CardState.REVIEW &&
                    card.nextReviewDate != null &&
                    card.nextReviewDate <= now
            }

        val newShownToday =
            cards.count { card ->
                card.cardState != CardState.NEW &&
                    card.firstReviewDate != null &&
                    card.firstReviewDate >= todayStart
            }
        val newLimit = (DAILY_NEW_LIMIT - newShownToday).coerceAtLeast(0)

        val new =
            cards
                .filter { it.cardState == CardState.NEW }
                .take(newLimit)

        return learning + review + new
    }

    companion object {
        private const val DAILY_NEW_LIMIT = 20
        const val LEARN_AHEAD_MILLIS = 5 * 60 * 1000L
    }
}
