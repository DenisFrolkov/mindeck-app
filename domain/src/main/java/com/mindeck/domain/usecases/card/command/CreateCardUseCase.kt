package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepository
import javax.inject.Inject

class CreateCardUseCase @Inject constructor (private val cardRepository: CardRepository) {
    suspend operator fun invoke(card: Card) {
        cardRepository.insertCard(card = card)
    }
}