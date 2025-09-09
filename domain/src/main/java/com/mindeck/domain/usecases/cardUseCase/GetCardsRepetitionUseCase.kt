package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepetitionRepository
import kotlinx.coroutines.flow.Flow

class GetCardsRepetitionUseCase(private val cardRepetitionRepository: CardRepetitionRepository) {
    operator fun invoke(currentTime: Long): Flow<List<Card>> {
        return cardRepetitionRepository.getCardsRepetition(currentTime = currentTime)
    }
}