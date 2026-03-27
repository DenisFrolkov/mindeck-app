package com.mimdeck.data.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.mimdeck.data.database.AppDatabase
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.entities.DeckEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeckDaoTest {
    private lateinit var db: AppDatabase

    private lateinit var deckDao: DeckDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()

        deckDao = db.deckDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun renameDeck_updatesName_and_getDeckById_returnsRenamed() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        deckDao.renameDeck(deckId = 1, newName = "Deck New")

        deckDao.getDeckById(1).test {
            val result = awaitItem()
            assertEquals("Deck New", result?.deckName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertDeck_and_getDeckById_returnsInsertedDeck() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        deckDao.getDeckById(1).test {
            assertEquals(deck, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteDeck_removesDeck_and_getAllDecks_returnsRemaining() = runTest {
        val deck1 = DeckEntity(deckId = 1, deckName = "Deck 1")
        val deck2 = DeckEntity(deckId = 2, deckName = "Deck 2")
        deckDao.insertDeck(deck1)
        deckDao.insertDeck(deck2)

        deckDao.deleteDeck(1)

        deckDao.getAllDecks().test {
            assertEquals(listOf(deck2), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertDeck_withDuplicateName_throwsSQLiteConstraintException() = runTest {
        deckDao.insertDeck(DeckEntity(deckName = "Same Name"))
        deckDao.insertDeck(DeckEntity(deckName = "Same Name"))
    }
}
