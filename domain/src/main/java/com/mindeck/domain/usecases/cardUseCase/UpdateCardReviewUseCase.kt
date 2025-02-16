package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import com.mindeck.domain.repository.CardRepository

class UpdateCardReviewUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(
        cardId: Int,
        currentTime: Long,
        newReviewDate: Long,
        newRepetitionCount: Int,
        reviewType: ReviewType
    ) {
        return cardRepository.updateReview(
            cardId,
            currentTime,
            newReviewDate,
            newRepetitionCount,
            reviewType
        )
    }
}