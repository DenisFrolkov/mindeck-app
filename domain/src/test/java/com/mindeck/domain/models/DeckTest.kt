package com.mindeck.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DeckTest {
    private fun validDeck(
        deckName: String = "Столицы мира",
    ) = Deck(
        deckName = deckName,
    )

    @Test
    fun `deck with blank name throws IllegalArgumentException`() {
        val blankName = "   "

        val exception = assertThrows(IllegalArgumentException::class.java) {
            validDeck(deckName = blankName)
        }

        assertEquals("Deck name must not be blank", exception.message)
    }

    @Test
    fun `valid deck is created successfully`() {
        val deck = validDeck()
        assertEquals("Столицы мира", deck.deckName)
    }
}
