package com.mimdeck.data.repository

import com.mimdeck.data.dataSource.DeckDataSource
import com.mimdeck.data.database.entities.Mappers.toData
import com.mimdeck.data.database.entities.Mappers.toDomain
import com.mimdeck.data.exception.DatabaseException
import com.mindeck.domain.exception.DomainException
import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeckRepositoryImpl @Inject constructor(private val deckDataSource: DeckDataSource) : DeckRepository {
    override suspend fun insertDeck(deck: Deck) {
        try {
            deckDataSource.insertDeck(deckEntity = deck.toData())
        } catch (e: DatabaseException) {
            throw DomainException("Failed to create deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun renameDeck(deckId: Int, newName: String) {
        try {
            deckDataSource.renameDeck(deckId = deckId, newName = newName)
        } catch (e: DatabaseException) {
            throw DomainException("Failed to rename deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteDeck(deck: Deck) {
        try {
            deckDataSource.deleteDeck(deckEntity = deck.toData())
        } catch (e: DatabaseException) {
            throw DomainException("Failed to delete deck: ${e.localizedMessage}", e)
        }
    }

    override fun getAllDecks(): Flow<List<Deck>> {
        return try {
            deckDataSource.getAllDecks()
                .map { decksEntityList -> decksEntityList.map { it.toDomain() } }
        } catch (e: DatabaseException) {
            flow { emit(emptyList<Deck>()) }
            throw DomainException("Failed get all decks by folder id", e)
        }
    }

    override suspend fun getDeckById(deckId: Int): Deck {
        return try {
            deckDataSource.getDeckById(deckId = deckId).toDomain()
        } catch (e: DatabaseException) {
            throw DomainException("Failed get deck", e)
        }
    }
}