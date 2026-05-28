package com.mindeck.domain.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeckTest {
    private fun validDeck(
        deckName: String = "Столицы мира",
    ) = Deck(
        deckName = deckName,
    )

    @Test
    fun `deck with blank name throws IllegalArgumentException`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validDeck(deckName = "   ")
        }
        assertEquals("Deck name must not be blank", exception.message)
    }

    @Test
    fun `valid deck is created successfully`() {
        val deck = validDeck()
        assertEquals("Столицы мира", deck.deckName)
    }
}
