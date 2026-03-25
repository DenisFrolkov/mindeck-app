package com.mimdeck.data.repository

import app.cash.turbine.test
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.mapper.Mappers.toEntity
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CardRepetitionRepositoryImplTest {
    private val DAY_MS = 86_400_000L
    private val now = DAY_MS * 10 + DAY_MS / 2
    private val todayStart = DAY_MS * 10

    private val card = Card(
        cardId = 1,
        cardName = "Card 1",
        cardQuestion = "Question 1",
        cardAnswer = "Answer 1",
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
    )
    private val cardDao = mockk<CardDao>()

    private val repository = CardRepetitionRepositoryImpl(cardDao)

    @Test
    fun `getCardsRepetition emits mapped card list when dao returns entities`() = runTest {
        val cardEntity = card.toEntity()

        every { cardDao.getCardsRepetition(any(), any()) } returns flowOf(listOf(cardEntity))

        repository.getCardsRepetition(now, todayStart).test {
            val result = awaitItem()
            assertEquals(listOf(card), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCardsRepetition emits DatabaseError when dao flow throws Exception`() = runTest {
        every { cardDao.getCardsRepetition(any(), any()) } returns flow { throw Exception() }

        repository.getCardsRepetition(now, todayStart).test {
            val error = awaitError()
            assertTrue(error is DomainError.DatabaseError)
        }
    }

    @Test
    fun `updateCard calls dao with correct arguments`() = runTest {
        coEvery { cardDao.updateCard(any()) } just Runs
        repository.updateReview(card)

        coVerify { cardDao.updateCard(card.toEntity()) }
    }

    @Test(expected = DomainError.DatabaseError::class)
    fun `updateCard throws DatabaseError when dao throws Exception`() = runTest {
        coEvery { cardDao.updateCard(any()) } throws Exception()
        repository.updateReview(card)
    }
}
