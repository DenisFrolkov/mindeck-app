package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardByIdUseCase @Inject constructor(private val cardRepository: CardRepository) {
    operator fun invoke(cardId: Int): Flow<Card?> = cardRepository.getCardById(cardId = cardId)
}
