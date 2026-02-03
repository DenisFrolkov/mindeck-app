package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepetitionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardsRepetitionUseCase @Inject constructor(private val cardRepetitionRepository: CardRepetitionRepository) {
    operator fun invoke(currentTime: Long): Flow<List<Card>> {
        return cardRepetitionRepository.getCardsRepetition(currentTime = currentTime)
    }
}
