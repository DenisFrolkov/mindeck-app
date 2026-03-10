package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.ClockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardsRepetitionUseCase @Inject constructor(
    private val cardRepetitionRepository: CardRepetitionRepository,
    private val clock: ClockRepository,
) {
    operator fun invoke(): Flow<List<Card>> {
        return cardRepetitionRepository.getCardsRepetition(currentTime = clock.now())
    }
}
