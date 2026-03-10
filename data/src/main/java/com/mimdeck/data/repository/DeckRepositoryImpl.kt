package com.mimdeck.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.mapper.Mappers.toDomain
import com.mimdeck.data.database.mapper.Mappers.toEntity
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Deck
import com.mindeck.domain.repository.DeckRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeckRepositoryImpl @Inject constructor(
    private val deckDao: DeckDao,
) : DeckRepository {
    override suspend fun insertDeck(deck: Deck): Int = try {
        deckDao.insertDeck(deckEntity = deck.toEntity()).toInt()
    } catch (e: CancellationException) {
        throw e
    } catch (e: SQLiteConstraintException) {
        throw DomainError.NameAlreadyExists()
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }

    override suspend fun renameDeck(deckId: Int, newName: String) = try {
        deckDao.renameDeck(deckId = deckId, newName = newName)
    } catch (e: CancellationException) {
        throw e
    } catch (e: SQLiteConstraintException) {
        throw DomainError.NameAlreadyExists()
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }

    override suspend fun deleteDeck(deckId: Int) = try {
        deckDao.deleteDeck(deckId = deckId)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }

    override fun getAllDecks(): Flow<List<Deck>> =
        deckDao.getAllDecks()
            .map { list -> list.map { it.toDomain() } }
            .catch {
                if (it is CancellationException) throw it
                throw DomainError.DatabaseError()
            }

    override fun getDeckById(deckId: Int): Flow<Deck?> =
        deckDao.getDeckById(deckId = deckId)
            .map { it?.toDomain() }
            .catch {
                if (it is CancellationException) throw it
                throw DomainError.DatabaseError()
            }
}
