package com.mindeck.data.repository

import android.database.sqlite.SQLiteConstraintException
import app.cash.turbine.test
import com.mindeck.data.database.dao.DeckDao
import com.mindeck.data.database.entities.DeckEntity
import com.mindeck.data.database.mapper.Mappers.toDomain
import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.models.Deck
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
import org.junit.Test

class DeckRepositoryImplTest {
    private val deckDao = mockk<DeckDao>()

    private val repository = DeckRepositoryImpl(deckDao)

    @Test
    fun `insertDeck returns id from dao`() = runTest {
        val deck = Deck(deckId = 0, deckName = "Kotlin Basics")
        coEvery { deckDao.insertDeck(any()) } returns 42L

        val result = repository.insertDeck(deck)

        assertEquals(42, result)
    }

    @Test(expected = DomainError.NameAlreadyExists::class)
    fun `insertDeck throws NameAlreadyExists when dao throws SQLiteConstraintException`() = runTest {
        val deck = Deck(deckId = 0, deckName = "Kotlin Basics")
        coEvery { deckDao.insertDeck(any()) } throws SQLiteConstraintException()

        repository.insertDeck(deck)
    }

    @Test(expected = DomainError.DatabaseError::class)
    fun `insertDeck throws DatabaseError when dao throws Exception`() = runTest {
        val deck = Deck(deckId = 0, deckName = "Kotlin Basics")
        coEvery { deckDao.insertDeck(any()) } throws Exception()

        repository.insertDeck(deck)
    }

    @Test
    fun `renameDeck calls dao with correct arguments`() = runTest {
        coEvery { deckDao.renameDeck(any(), any()) } just Runs
        repository.renameDeck(deckId = 1, newName = "New Name")

        coVerify { deckDao.renameDeck(deckId = 1, newName = "New Name") }
    }

    @Test(expected = DomainError.NameAlreadyExists::class)
    fun `renameDeck throws NameAlreadyExists when dao throws SQLiteConstraintException`() = runTest {
        coEvery { deckDao.renameDeck(any(), any()) } throws SQLiteConstraintException()
        repository.renameDeck(deckId = 1, newName = "New Name")
    }

    @Test(expected = DomainError.DatabaseError::class)
    fun `renameDeck throws DatabaseError when dao throws Exception`() = runTest {
        coEvery { deckDao.renameDeck(any(), any()) } throws Exception()
        repository.renameDeck(deckId = 1, newName = "New Name")
    }

    @Test
    fun `deleteDeck calls dao with correct arguments`() = runTest {
        coEvery { deckDao.deleteDeck(any()) } just Runs
        repository.deleteDeck(deckId = 1)

        coVerify { deckDao.deleteDeck(deckId = 1) }
    }

    @Test(expected = DomainError.DatabaseError::class)
    fun `deleteDeck throws DatabaseError when dao throws Exception`() = runTest {
        coEvery { deckDao.deleteDeck(any()) } throws Exception()
        repository.deleteDeck(deckId = 1)
    }

    @Test
    fun `getAllDecks emits mapped domain list from dao`() = runTest {
        val deckEntity = DeckEntity(deckId = 1, deckName = "Deck 1")
        every { deckDao.getAllDecks() } returns flowOf(listOf(deckEntity))

        repository.getAllDecks().test {
            val result = awaitItem()
            assertEquals(listOf(deckEntity.toDomain()), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllDecks emits DatabaseError when dao flow throws Exception`() = runTest {
        every { deckDao.getAllDecks() } returns flow { throw Exception() }

        repository.getAllDecks().test {
            val error = awaitError()
            assert(error is DomainError.DatabaseError)
        }
    }

    @Test
    fun `getDeckById emits mapped deck when dao returns entity`() = runTest {
        val deckEntity = DeckEntity(deckId = 1, deckName = "Deck 1")
        every {
            deckDao.getDeckById(any())
        } returns flowOf(deckEntity)

        repository.getDeckById(1).test {
            val result = awaitItem()
            assertEquals(deckEntity.toDomain(), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getDeckById emits DatabaseError when dao flow throws Exception`() = runTest {
        every { deckDao.getDeckById(any()) } returns flow { throw Exception() }

        repository.getDeckById(1).test {
            val error = awaitError()
            assert(error is DomainError.DatabaseError)
        }
    }
}
