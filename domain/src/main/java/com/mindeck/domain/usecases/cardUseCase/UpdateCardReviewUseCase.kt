package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType.*
import com.mindeck.domain.repository.CardRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class UpdateCardReviewUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(card: Card) {
        val zoneId = ZoneId.systemDefault()
        val localDateTime = ZonedDateTime.ofInstant(Instant.now(), zoneId)
        val currentTimeMillis = localDateTime.toInstant().toEpochMilli()

        val firstReviewDate = card.firstReviewDate ?: currentTimeMillis

        val nextReviewDate = when (card.repetitionCount) {
            0 -> currentTimeMillis + TimeUnit.MINUTES.toMillis(1)
            1 -> {
                when (card.lastReviewType!!) {
                    REPEAT -> currentTimeMillis + TimeUnit.MINUTES.toMillis(10)
                    EASY -> currentTimeMillis + TimeUnit.DAYS.toMillis(5)
                    MEDIUM -> currentTimeMillis + TimeUnit.DAYS.toMillis(3)
                    HARD -> currentTimeMillis + TimeUnit.DAYS.toMillis(1)
                }
            }
            else -> {
                val interval = currentTimeMillis - card.firstReviewDate!!
                val multiplier = when (card.lastReviewType!!) {
                    EASY -> 2.5
                    MEDIUM -> 1.8
                    HARD -> 1.3
                    REPEAT -> 0.5
                }
                card.firstReviewDate + (interval * multiplier).toLong()
            }
        }

        cardRepository.updateCard(
            card.copy(
                firstReviewDate = firstReviewDate,
                lastReviewDate = currentTimeMillis,
                nextReviewDate = nextReviewDate,
                repetitionCount = card.repetitionCount + 1,
                lastReviewType = card.lastReviewType
            )
        )
    }
}