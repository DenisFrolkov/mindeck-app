package com.mindeck.domain.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
    fun `card with blank name is allowed`() {
        val card = validCard(cardName = "   ")
        assertEquals("   ", card.cardName)
    }

    @Test
    fun `card with blank question throws IllegalArgumentException`() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                validCard(cardQuestion = "   ")
            }
        assertEquals("Card question must not be blank", exception.message)
    }

    @Test
    fun `card with blank answer throws IllegalArgumentException`() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                validCard(cardAnswer = "   ")
            }
        assertEquals("Card answer must not be blank", exception.message)
    }

    @Test
    fun `valid card is created successfully`() {
        val card = validCard()
        assertEquals("Столица Франции", card.cardName)
    }
}
