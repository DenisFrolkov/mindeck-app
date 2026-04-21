package com.mindeck.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.mindeck.data.database.dao.CardDao
import com.mindeck.data.database.mapper.Mappers.toDomain
import com.mindeck.data.database.mapper.Mappers.toEntity
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardDao: CardDao,
) : CardRepository {
    override suspend fun insertCard(card: Card) = try {
        cardDao.insertCard(cardEntity = card.toEntity())
    } catch (e: CancellationException) {
        throw e
    } catch (e: SQLiteConstraintException) {
        throw DomainError.NameAlreadyExists()
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }

    override suspend fun updateCard(card: Card) = try {
        cardDao.updateCard(cardEntity = card.toEntity())
    } catch (e: CancellationException) {
        throw e
    } catch (e: SQLiteConstraintException) {
        throw DomainError.NameAlreadyExists()
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }

    override suspend fun deleteCard(cardId: Int) = try {
        cardDao.deleteCard(cardId = cardId)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }

    override fun getAllCardsByDeckId(deckId: Int): Flow<List<Card>> =
        cardDao.getAllCardsByDeckId(deckId = deckId)
            .map { list -> list.map { it.toDomain() } }
            .catch {
                if (it is CancellationException) throw it
                throw DomainError.DatabaseError()
            }

    override fun getCardById(cardId: Int): Flow<Card?> =
        cardDao.getCardById(cardId = cardId)
            .map { it?.toDomain() }
            .catch {
                if (it is CancellationException) throw it
                throw DomainError.DatabaseError()
            }

    override fun getCardWithDeckById(cardId: Int): Flow<CardWithDeck?> =
        cardDao.getCardWithDeckById(cardId = cardId)
            .map { it?.toDomain() }
            .catch {
                if (it is CancellationException) throw it
                throw DomainError.DatabaseError()
            }
}
