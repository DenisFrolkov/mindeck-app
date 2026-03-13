package com.mimdeck.data.repository

import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.mapper.Mappers.toDomain
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
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

    override suspend fun updateReview(
        cardId: Int,
        cardState: CardState,
        easeFactor: Float,
        interval: Float,
        learningStep: Int,
        nextReviewDate: Long,
        repetitionCount: Int,
        lapseCount: Int,
        firstReviewDate: Long,
        lastReviewDate: Long,
    ) = try {
        cardDao.updateReview(
            cardId = cardId,
            cardState = cardState.name,
            easeFactor = easeFactor,
            interval = interval,
            learningStep = learningStep,
            nextReviewDate = nextReviewDate,
            repetitionCount = repetitionCount,
            lapseCount = lapseCount,
            firstReviewDate = firstReviewDate,
            lastReviewDate = lastReviewDate,
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw DomainError.DatabaseError()
    }
}
