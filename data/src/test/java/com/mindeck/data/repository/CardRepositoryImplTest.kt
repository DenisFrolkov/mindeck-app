package com.mindeck.data.repository

import android.database.sqlite.SQLiteConstraintException
import app.cash.turbine.test
import com.mindeck.data.database.dao.CardDao
import com.mindeck.data.database.entities.CardWithDeckEntity
import com.mindeck.data.database.entities.DeckEntity
import com.mindeck.data.database.mapper.Mappers.toEntity
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
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

class CardRepositoryImplTest {

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

    private val repository = CardRepositoryImpl(cardDao)

    @Test
    fun `insertCard calls dao with correct arguments`() = runTest {
        coEvery { cardDao.insertCard(any()) } just Runs
        repository.insertCard(card)

        coVerify { cardDao.insertCard(card.toEntity()) }
    }

    @Test(expected = DomainError.NameAlreadyExists::class)
    fun `insertCard throws NameAlreadyExists when dao throws SQLiteConstraintException`() = runTest {
        coEvery { cardDao.insertCard(any()) } throws SQLiteConstraintException()
        repository.insertCard(card)
    }

    @Test(expected = DomainError.DatabaseError::class)
    fun `insertCard throws DatabaseError when dao throws Exception`() = runTest {
        coEvery { cardDao.insertCard(any()) } throws Exception()
        repository.insertCard(card)
    }

    @Test
    fun `updateCard calls dao with correct arguments`() = runTest {
        coEvery { cardDao.updateCard(any()) } just Runs
        repository.updateCard(card)

        coVerify { cardDao.updateCard(card.toEntity()) }
    }

    @Test(expected = DomainError.NameAlreadyExists::class)
    fun `updateCard throws NameAlreadyExists when dao throws SQLiteConstraintException`() = runTest {
        coEvery { cardDao.updateCard(any()) } throws SQLiteConstraintException()
        repository.updateCard(card)
    }

    @Test(expected = DomainError.DatabaseError::class)
    fun `updateCard throws DatabaseError when dao throws Exception`() = runTest {
        coEvery { cardDao.updateCard(any()) } throws Exception()
        repository.updateCard(card)
    }

    @Test
    fun `deleteCard calls dao with correct arguments`() = runTest {
        coEvery { cardDao.deleteCard(any()) } just Runs
        repository.deleteCard(1)

        coVerify { cardDao.deleteCard(1) }
    }

    @Test(expected = DomainError.DatabaseError::class)
    fun `deleteCard throws DatabaseError when dao throws Exception`() = runTest {
        coEvery { cardDao.deleteCard(1) } throws Exception()
        repository.deleteCard(1)
    }

    @Test
    fun `getAllCardsByDeckId emits mapped card list when dao returns entities`() = runTest {
        val cardEntity = card.toEntity()

        every { cardDao.getAllCardsByDeckId(any()) } returns flowOf(listOf(cardEntity))

        repository.getAllCardsByDeckId(1).test {
            val result = awaitItem()
            assertEquals(listOf(card), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllCardsByDeckId emits DatabaseError when dao flow throws Exception`() = runTest {
        every { cardDao.getAllCardsByDeckId(any()) } returns flow { throw Exception() }

        repository.getAllCardsByDeckId(1).test {
            val error = awaitError()
            assertTrue(error is DomainError.DatabaseError)
        }
    }

    @Test
    fun `getCardById emits mapped card when dao returns entity`() = runTest {
        val cardEntity = card.toEntity()

        every { cardDao.getCardById(any()) } returns flowOf(cardEntity)

        repository.getCardById(1).test {
            val result = awaitItem()
            assertEquals(card, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCardById emits DatabaseError when dao flow throws Exception`() = runTest {
        every { cardDao.getCardById(any()) } returns flow { throw Exception() }

        repository.getCardById(1).test {
            val error = awaitError()
            assertTrue(error is DomainError.DatabaseError)
        }
    }

    @Test
    fun `getCardWithDeckById emits mapped card with deck when dao returns entity`() = runTest {
        val cardWithDeckEntity = CardWithDeckEntity(
            card = card.toEntity(),
            deck = DeckEntity(deckId = 1, deckName = "Deck 1"),
        )

        every { cardDao.getCardWithDeckById(any()) } returns flowOf(cardWithDeckEntity)

        repository.getCardWithDeckById(1).test {
            val result = awaitItem()
            val expected = CardWithDeck(card = card, deckId = 1, deckName = "Deck 1")
            assertEquals(expected, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCardWithDeckById emits DatabaseError when dao flow throws Exception`() = runTest {
        every { cardDao.getCardWithDeckById(any()) } returns flow { throw Exception() }

        repository.getCardWithDeckById(1).test {
            val error = awaitError()
            assertTrue(error is DomainError.DatabaseError)
        }
    }
}
