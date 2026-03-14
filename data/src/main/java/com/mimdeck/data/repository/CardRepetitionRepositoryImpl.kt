package com.mimdeck.data.repository

import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.mapper.Mappers.toDomain
import com.mimdeck.data.database.mapper.Mappers.toEntity
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepetitionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CardRepetitionRepositoryImpl @Inject constructor(
    private val cardDao: CardDao,
) : CardRepetitionRepository {
    override fun getCardsRepetition(currentTime: Long, todayStart: Long): Flow<List<Card>> =
        cardDao.getCardsRepetition(currentTime = currentTime, todayStart = todayStart)
            .map { cardsEntityList -> cardsEntityList.map { it.toDomain() } }
            .catch { e ->
                if (e is CancellationException) throw e
                throw DomainError.DatabaseError()
            }

    override suspend fun updateReview(card: Card) = try {
        cardDao.updateCard(card.toEntity())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }
}
