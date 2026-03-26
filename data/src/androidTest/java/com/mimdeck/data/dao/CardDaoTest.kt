package com.mimdeck.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.mimdeck.data.database.AppDatabase
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.DeckEntity
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var cardDao: CardDao
    private lateinit var deckDao: DeckDao

    private val DAY_MS = 86_400_000L
    private val now = DAY_MS * 10 + DAY_MS / 2
    private val todayStart = DAY_MS * 10


    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()

        cardDao = db.cardDao()
        deckDao = db.deckDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertCard_and_getCardById_returns_inserted_card() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardTag = "",
            deckId = 1,
        )

        cardDao.insertCard(card)

        cardDao.getCardById(1).test {
            assertEquals(card, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getCardsRepetition_returns_card_with_NEW_state() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            nextReviewDate = now + todayStart,
            cardTag = "",
            deckId = 1,
        )

        cardDao.insertCard(card)

        cardDao.getCardsRepetition(now, todayStart).test {
            val result = awaitItem()
            assertEquals(listOf(card), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getCardsRepetition_returns_card_with_REVIEW_state_and_past_nextReviewDate() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            nextReviewDate = now - DAY_MS,
            cardTag = "",
            deckId = 1,
        )

        cardDao.insertCard(card)

        cardDao.getCardsRepetition(now, todayStart).test {
            val result = awaitItem()
            assertEquals(listOf(card), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getCardsRepetition_does_not_return_card_with_future_nextReviewDate() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )

        cardDao.insertCard(card)

        cardDao.getCardsRepetition(now, todayStart).test {
            val result = awaitItem()
            assertEquals(emptyList<CardEntity>(), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getCardsRepetition_returns_card_with_firstReviewDate_today() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )

        cardDao.insertCard(card)

        cardDao.getCardsRepetition(now, todayStart).test {
            val result = awaitItem()
            assertEquals(listOf(card), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getCardsRepetition_does_not_return_card_with_firstReviewDate_yesterday() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )

        cardDao.insertCard(card)

        cardDao.getCardsRepetition(now, todayStart).test {
            val result = awaitItem()
            assertEquals(emptyList<CardEntity>(), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteDeck_cascadeDeletes_allCardsInDeck() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )
        cardDao.insertCard(card)

        deckDao.deleteDeck(1)

        cardDao.getAllCardsByDeckId(1).test {
            val result = awaitItem()
            assertEquals(emptyList<CardEntity>(), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateCard_updatesFields_and_getCardById_returnsUpdated() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )
        cardDao.insertCard(card)

        val updateCard = card.copy(cardState = "LEARNING", easeFactor = 3f, interval = 2f, nextReviewDate = now + DAY_MS * 4)

        cardDao.updateCard(updateCard)

        cardDao.getCardById(1).test {
            val result = awaitItem()
            assertEquals(updateCard, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteCard_removesCard_and_getCardById_returnsNull() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )
        cardDao.insertCard(card)

        cardDao.deleteCard(1)

        cardDao.getCardById(1).test {
            val result = awaitItem()
            assertEquals(null, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getAllCardsByDeckId_returnsOnly_cardsFromRequestedDeck() = runTest {
        val deck1 = DeckEntity(deckId = 1, deckName = "Test Deck 1")
        deckDao.insertDeck(deck1)

        val deck2 = DeckEntity(deckId = 2, deckName = "Test Deck 2")
        deckDao.insertDeck(deck2)

        val card1 = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )
        cardDao.insertCard(card1)

        val card2 = CardEntity(
            cardId = 2,
            cardName = "Card 2",
            cardQuestion = "Q2?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )
        cardDao.insertCard(card2)

        val card3 = CardEntity(
            cardId = 3,
            cardName = "Card 3",
            cardQuestion = "Q3?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 2,
        )
        cardDao.insertCard(card3)


        cardDao.getAllCardsByDeckId(1).test {
            val result = awaitItem()
            assertEquals(listOf<CardEntity>(card1, card2), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getCardWithDeckById_returnsCardWithCorrectDeck() = runTest {
        val deck = DeckEntity(deckId = 1, deckName = "Test Deck 1")
        deckDao.insertDeck(deck)

        val card = CardEntity(
            cardId = 1,
            cardName = "Card 1",
            cardQuestion = "Q?",
            cardAnswer = "A",
            cardType = CardType.SIMPLE.stableId,
            cardState = "REVIEW",
            firstReviewDate = todayStart - DAY_MS,
            nextReviewDate = now + DAY_MS,
            cardTag = "",
            deckId = 1,
        )
        cardDao.insertCard(card)

        cardDao.getCardWithDeckById(1).test {
            val result = awaitItem()
            assertEquals(card, result?.card)
            assertEquals(deck, result?.deck)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
