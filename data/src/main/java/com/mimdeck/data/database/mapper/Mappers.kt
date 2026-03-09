package com.mimdeck.data.database.mapper

import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.CardWithDeckEntity
import com.mimdeck.data.database.entities.DeckEntity
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.ReviewType

object Mappers {

    fun Deck.toEntity(): DeckEntity {
        return DeckEntity(
            deckId = this.deckId,
            deckName = this.deckName,
        )
    }

    fun DeckEntity.toDomain(): Deck {
        return Deck(
            deckId = this.deckId,
            deckName = this.deckName,
        )
    }

    fun Card.toEntity(): CardEntity {
        return CardEntity(
            cardId = this.cardId,
            cardName = this.cardName,
            cardQuestion = this.cardQuestion,
            cardAnswer = this.cardAnswer,
            cardType = this.cardType.stableId,
            cardTag = this.cardTag,
            deckId = this.deckId,
            firstReviewDate = this.firstReviewDate,
            lastReviewDate = this.lastReviewDate,
            nextReviewDate = this.nextReviewDate,
            repetitionCount = this.repetitionCount,
            lastReviewType = this.lastReviewType?.name,
        )
    }

    fun CardEntity.toDomain(): Card {
        return Card(
            cardId = this.cardId,
            cardName = this.cardName,
            cardQuestion = this.cardQuestion,
            cardAnswer = this.cardAnswer,
            cardType = CardType.fromStableId(this.cardType),
            cardTag = this.cardTag,
            deckId = this.deckId,
            firstReviewDate = this.firstReviewDate,
            lastReviewDate = this.lastReviewDate,
            nextReviewDate = this.nextReviewDate,
            repetitionCount = this.repetitionCount,
            lastReviewType = this.lastReviewType?.let { ReviewType.fromString(it) },
        )
    }

    fun CardWithDeckEntity.toDomain(): CardWithDeck {
        return CardWithDeck(
            card = this.card.toDomain(),
            deckName = this.deck.deckName,
        )
    }
}
