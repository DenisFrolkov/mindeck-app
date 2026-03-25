package com.mimdeck.data.database.mapper

import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.CardWithDeckEntity
import com.mimdeck.data.database.entities.DeckEntity
import com.mimdeck.data.database.mapper.Mappers.toDomain
import com.mimdeck.data.database.mapper.Mappers.toEntity
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.models.Deck
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    private val deck = Deck(
        deckId = 0,
        deckName = "Deck 1",
    )

    private val deckEntity = DeckEntity(
        deckId = 0,
        deckName = "Deck 1",
    )

    private val card = Card(
        cardId = 1,
        cardName = "Card 1",
        cardQuestion = "Question 1",
        cardAnswer = "Answer 1",
        cardType = CardType.SIMPLE,
        cardTag = "",
        deckId = 1,
    )

    private val cardEntity = CardEntity(
        cardId = 1,
        cardName = "Card 1",
        cardQuestion = "Question 1",
        cardAnswer = "Answer 1",
        cardType = CardType.SIMPLE.stableId,
        cardTag = "",
        deckId = 1,
    )

    private val cardWithDeckEntity = CardWithDeckEntity(
        card = cardEntity,
        deck = deckEntity,
    )

    private val cardWithDeck = CardWithDeck(
        card = card,
        deckId = deck.deckId,
        deckName = deck.deckName,
    )

    @Test
    fun `Deck toEntity maps all fields correctly`() {
        assertEquals(deckEntity, deck.toEntity())
    }

    @Test
    fun `DeckEntity toDomain maps all fields correctly`() {
        assertEquals(deck, deckEntity.toDomain())
    }

    @Test
    fun `Card toEntity maps all fields correctly`() {
        assertEquals(cardEntity, card.toEntity())
    }

    @Test
    fun `CardEntity toDomain maps all fields correctly`() {
        assertEquals(card, cardEntity.toDomain())
    }

    @Test
    fun `CardWithDeckEntity toDomain maps all fields correctly`() {
        assertEquals(cardWithDeck, cardWithDeckEntity.toDomain())
    }
}
