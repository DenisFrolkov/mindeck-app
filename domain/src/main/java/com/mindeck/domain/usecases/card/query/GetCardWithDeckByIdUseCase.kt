package com.mindeck.domain.usecases.card.query

import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardWithDeckByIdUseCase @Inject constructor(
    private val cardRepository: CardRepository,
) {
    operator fun invoke(cardId: Int): Flow<CardWithDeck?> = cardRepository.getCardWithDeckById(cardId = cardId)
}
