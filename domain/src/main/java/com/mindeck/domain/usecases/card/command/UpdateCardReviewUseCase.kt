package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.models.ReviewType
import com.mindeck.domain.repository.CardRepetitionRepository
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateCardReviewUseCase @Inject constructor (private val cardRepetitionRepository: CardRepetitionRepository) {
    suspend operator fun invoke(
        cardId: Int,
        firstReviewDate: Long?,
        newRepetitionCount: Int,
        lastReviewType: ReviewType
    ) {
        val now = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val firstReview = firstReviewDate ?: now
        val nextReview = calculateNextReviewDate(firstReview, now, newRepetitionCount, lastReviewType)

        cardRepetitionRepository.updateReview(
            cardId,
            firstReview,
            now,
            nextReview,
            newRepetitionCount + 1,
            lastReviewType
        )
    }

    private fun calculateNextReviewDate(
        firstReviewDate: Long,
        currentTimeMillis: Long,
        newRepetitionCount: Int,
        lastReviewType: ReviewType
    ): Long {
        return if (newRepetitionCount <= 1) {
            currentTimeMillis + when (lastReviewType) {
                ReviewType.REPEAT -> TimeUnit.MINUTES.toMillis(10)
                ReviewType.EASY -> TimeUnit.DAYS.toMillis(5)
                ReviewType.MEDIUM -> TimeUnit.DAYS.toMillis(3)
                ReviewType.HARD -> TimeUnit.DAYS.toMillis(1)
            }
        } else {
            val interval = currentTimeMillis - firstReviewDate
            val multiplier = when (lastReviewType) {
                ReviewType.EASY -> 2.5
                ReviewType.MEDIUM -> 2.0
                ReviewType.HARD -> 1.5
                ReviewType.REPEAT -> 1.1
            }
            currentTimeMillis + (interval * multiplier).toLong()
        }
    }
}