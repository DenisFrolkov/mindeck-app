package com.mimdeck.data.repository

import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.mapper.Mappers.toDomain
import com.mimdeck.data.repository.util.handleDatabase
import com.mimdeck.data.repository.util.handleDatabaseSuspend
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import com.mindeck.domain.repository.CardRepetitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CardRepetitionRepositoryImpl  @Inject constructor(
    private val cardDao: CardDao
) : CardRepetitionRepository {
    override fun getCardsRepetition(currentTime: Long): Flow<List<Card>> = handleDatabase {
        cardDao.getCardsRepetition(currentTime = currentTime)
            .map { cardsEntityList -> cardsEntityList.map { it.toDomain() } }
    }

    override suspend fun updateReview(
        cardId: Int,
        firstReviewDate: Long,
        lastReviewDate: Long,
        newReviewDate: Long,
        newRepetitionCount: Int,
        lastReviewType: ReviewType
    ) = handleDatabaseSuspend {
        cardDao.updateReview(
            cardId,
            firstReviewDate,
            lastReviewDate,
            newReviewDate,
            newRepetitionCount,
            lastReviewType
        )
    }
}