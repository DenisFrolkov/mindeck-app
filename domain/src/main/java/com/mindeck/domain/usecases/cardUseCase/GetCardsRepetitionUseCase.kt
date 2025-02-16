package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

class GetCardsRepetitionUseCase(private val cardRepository: CardRepository) {
    operator fun invoke(currentTime: Long): Flow<List<Card>> {
        return cardRepository.getCardsRepetition(currentTime = currentTime)
    }
}