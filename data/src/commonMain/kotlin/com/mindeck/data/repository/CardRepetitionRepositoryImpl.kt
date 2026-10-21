package com.mindeck.data.repository

import com.mindeck.data.dao.CardDao
import com.mindeck.data.mapper.Mappers.toDomain
import com.mindeck.data.mapper.Mappers.toEntity
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.repository.CardRepetitionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first

class CardRepetitionRepositoryImpl(
    private val cardDao: CardDao,
) : CardRepetitionRepository {
    override suspend fun getCardsRepetition(
        currentTime: Long,
        todayStart: Long,
    ): List<Card> =
        try {
            cardDao
                .getCardsRepetition(currentTime = currentTime, todayStart = todayStart)
                .first()
                .map { it.toDomain() }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw DomainError.DatabaseError()
        }

    override suspend fun getLearnAheadCard(until: Long): Card? =
        try {
            cardDao.getEarliestLearningCard(until)?.toDomain()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw DomainError.DatabaseError()
        }

    override suspend fun updateReview(card: Card) =
        try {
            cardDao.updateCard(card.toEntity())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw DomainError.DatabaseError()
        }
}
