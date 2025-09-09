package com.mimdeck.data.repository

import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.mapper.Mappers.toData
import com.mimdeck.data.database.mapper.Mappers.toDomain
import com.mimdeck.data.util.handleDatabase
import com.mimdeck.data.util.handleDatabaseSuspend
import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeckRepositoryImpl @Inject constructor(private val deckDao: DeckDao) : DeckRepository {
    override suspend fun insertDeck(deck: Deck) = handleDatabaseSuspend {
        deckDao.insertDeck(deckEntity = deck.toData())
    }

    override suspend fun renameDeck(deckId: Int, newName: String) = handleDatabaseSuspend {
        deckDao.renameDeck(deckId = deckId, newName = newName)
    }

    override suspend fun deleteDeck(deck: Deck) = handleDatabaseSuspend {
        deckDao.deleteDeck(deckEntity = deck.toData())
    }

    override fun getAllDecks(): Flow<List<Deck>> = handleDatabase {
        deckDao.getAllDecks()
            .map { decksEntityList -> decksEntityList.map { it.toDomain() } }
    }

    override suspend fun getDeckById(deckId: Int): Deck = handleDatabaseSuspend {
        deckDao.getDeckById(deckId = deckId).toDomain()
    }
}