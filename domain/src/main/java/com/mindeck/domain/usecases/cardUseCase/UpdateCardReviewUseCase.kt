package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.ReviewType
import com.mindeck.domain.models.ReviewType.EASY
import com.mindeck.domain.models.ReviewType.HARD
import com.mindeck.domain.models.ReviewType.MEDIUM
import com.mindeck.domain.models.ReviewType.REPEAT
import com.mindeck.domain.repository.CardRepository
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class UpdateCardReviewUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(
        cardId: Int,
        firstReviewDate: Long?,
        newRepetitionCount: Int,
        lastReviewType: ReviewType
    ) {
        val zoneId = ZoneId.systemDefault()
        val currentTimeMillis = ZonedDateTime.now(zoneId).toInstant().toEpochMilli()

        val firstReviewDateUpdate = firstReviewDate ?: currentTimeMillis

        val nextReviewDate = when {
            newRepetitionCount <= 1 -> {
                currentTimeMillis + when (lastReviewType) {
                    REPEAT -> TimeUnit.MINUTES.toMillis(10)
                    EASY -> TimeUnit.DAYS.toMillis(5)
                    MEDIUM -> TimeUnit.DAYS.toMillis(3)
                    HARD -> TimeUnit.DAYS.toMillis(1)
                }
            }
            else -> {
                val interval = currentTimeMillis - firstReviewDateUpdate
                val multiplier = when (lastReviewType) {
                    EASY -> 2.5
                    MEDIUM -> 2.0
                    HARD -> 1.5
                    REPEAT -> 1.1
                }
                currentTimeMillis + (interval * multiplier).toLong()
            }
        }

        cardRepository.updateReview(
            cardId,
            firstReviewDateUpdate,
            currentTimeMillis,
            nextReviewDate,
            newRepetitionCount + 1,
            lastReviewType
        )
    }
}