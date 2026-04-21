package com.mindeck.data.database.mapper

import com.mindeck.data.database.entities.CardEntity
import com.mindeck.data.database.entities.CardWithDeckEntity
import com.mindeck.data.database.entities.DeckEntity
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.models.Deck

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
            cardState = this.cardState.name,
            easeFactor = this.easeFactor,
            interval = this.interval,
            learningStep = this.learningStep,
            nextReviewDate = this.nextReviewDate,
            repetitionCount = this.repetitionCount,
            lapseCount = this.lapseCount,
            firstReviewDate = this.firstReviewDate,
            lastReviewDate = this.lastReviewDate,
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
            cardState = CardState.entries.firstOrNull { it.name == this.cardState } ?: CardState.NEW,
            easeFactor = this.easeFactor,
            interval = this.interval,
            learningStep = this.learningStep,
            nextReviewDate = this.nextReviewDate,
            repetitionCount = this.repetitionCount,
            lapseCount = this.lapseCount,
            firstReviewDate = this.firstReviewDate,
            lastReviewDate = this.lastReviewDate,
        )
    }

    fun CardWithDeckEntity.toDomain(): CardWithDeck {
        return CardWithDeck(
            card = this.card.toDomain(),
            deckId = this.deck.deckId,
            deckName = this.deck.deckName,
        )
    }
}
