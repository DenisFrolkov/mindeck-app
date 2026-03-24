package com.mindeck.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CardTest {
    private fun validCard(
        cardName: String = "Столица Франции",
        cardQuestion: String = "Какая столица Франции?",
        cardAnswer: String = "Париж",
    ) = Card(
        cardName = cardName,
        cardQuestion = cardQuestion,
        cardAnswer = cardAnswer,
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
    )

    @Test
    fun `card with blank name throws IllegalArgumentException`() {
        val blankName = "   "

        val exception = assertThrows(IllegalArgumentException::class.java) {
            validCard(cardName = blankName)
        }

        assertEquals("Card name must not be blank", exception.message)
    }

    @Test
    fun `card with blank question throws IllegalArgumentException`() {
        val blankQuestion = "   "

        val exception = assertThrows(IllegalArgumentException::class.java) {
            validCard(cardQuestion = blankQuestion)
        }

        assertEquals("Card question must not be blank", exception.message)
    }

    @Test
    fun `card with blank answer throws IllegalArgumentException`() {
        val blankAnswer = "   "

        val exception = assertThrows(IllegalArgumentException::class.java) {
            validCard(cardAnswer = blankAnswer)
        }

        assertEquals("Card answer must not be blank", exception.message)
    }

    @Test
    fun `valid card is created successfully`() {
        val card = validCard()
        assertEquals("Столица Франции", card.cardName)
    }
}
