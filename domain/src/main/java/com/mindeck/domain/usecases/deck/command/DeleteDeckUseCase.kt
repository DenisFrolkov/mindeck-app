package com.mindeck.domain.usecases.deck.command

import com.mindeck.domain.repository.DeckRepository
import javax.inject.Inject

class DeleteDeckUseCase @Inject constructor(private val deckRepository: DeckRepository) {
    suspend operator fun invoke(deckId: Int) {
        deckRepository.deleteDeck(deckId = deckId)
    }
}
