package com.mindeck.domain.usecases.cardUseCase

import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.CardRepository

class GetFolderByCardIdUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(cardId: Int): Folder? {
        return cardRepository.getFolderByCardId(cardId = cardId)
    }
}