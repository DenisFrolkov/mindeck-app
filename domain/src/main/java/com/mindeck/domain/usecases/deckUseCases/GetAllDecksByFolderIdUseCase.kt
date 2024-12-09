package com.mindeck.domain.usecases.deckUseCases

import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow

class GetAllDecksByFolderIdUseCase(private val deckRepository: DeckRepository) {
    operator fun invoke(folderId: Int): Flow<List<Deck>> {
        return deckRepository.getAllDecksByFolderId(folderId = folderId)
    }
}