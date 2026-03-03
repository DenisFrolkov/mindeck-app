package com.mimdeck.data.database.mapper

import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.DeckEntity
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.ReviewType
import com.mimdeck.data.database.entities.CardWithDeck as CardWithDeckEntity

object Mappers {

    fun Deck.toData(): DeckEntity {
        return DeckEntity(
            this.deckId,
            this.deckName,
        )
    }

    fun DeckEntity.toDomain(): Deck {
        return Deck(
            this.deckId,
            this.deckName ?: "Deck ${this.deckId}",
        )
    }

    fun Card.toData(): CardEntity {
        return CardEntity(
            this.cardId,
            this.cardName,
            this.cardQuestion,
            this.cardAnswer,
            this.cardType.stableId.toString(),
            this.cardTag,
            this.deckId,
            this.firstReviewDate,
            this.lastReviewDate,
            this.nextReviewDate,
            this.repetitionCount,
            this.lastReviewType?.name,
        )
    }

    fun CardEntity.toDomain(): Card {
        return Card(
            this.cardId,
            this.cardName ?: "Card ${this.cardId}",
            this.cardQuestion ?: "Question №${this.cardId}",
            this.cardAnswer,
            CardType.fromStableId(this.cardType.toIntOrNull() ?: 1),
            this.cardTag,
            this.deckId,
            this.firstReviewDate,
            this.lastReviewDate,
            this.nextReviewDate,
            this.repetitionCount,
            this.lastReviewType?.toData(),
        )
    }

    fun String.toData(): ReviewType {
        return when (this) {
            "Normal" -> ReviewType.REPEAT
            "Easy" -> ReviewType.EASY
            "Medium" -> ReviewType.MEDIUM
            "Hard" -> ReviewType.HARD
            else -> ReviewType.REPEAT
        }
    }

    fun CardWithDeckEntity.toDomain(): CardWithDeck {
        return CardWithDeck(
            card = this.card.toDomain(),
            deckName = this.deck.deckName ?: "Deck ${this.deck.deckId}",
        )
    }
}
