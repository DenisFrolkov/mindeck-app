package com.mindeck.data.mapper

import com.mindeck.data.entities.CardEntity
import com.mindeck.data.entities.CardWithDeckEntity
import com.mindeck.data.entities.DeckEntity
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.DeckColor

object Mappers {
    fun Deck.toEntity(): DeckEntity =
        DeckEntity(
            deckId = deckId,
            deckName = deckName,
            deckColor = deckColor.name,
        )

    fun DeckEntity.toDomain(): Deck =
        Deck(
            deckId = deckId,
            deckName = deckName,
            deckColor = enumValueOf<DeckColor>(deckColor),
        )

    fun Card.toEntity(): CardEntity =
        CardEntity(
            cardId = cardId,
            cardName = cardName,
            cardQuestion = cardQuestion,
            cardAnswer = cardAnswer,
            cardType = cardType.stableId,
            cardTag = cardTag,
            deckId = deckId,
            cardState = cardState.name,
            easeFactor = easeFactor,
            interval = interval,
            learningStep = learningStep,
            nextReviewDate = nextReviewDate,
            repetitionCount = repetitionCount,
            lapseCount = lapseCount,
            firstReviewDate = firstReviewDate,
            lastReviewDate = lastReviewDate,
        )

    fun CardEntity.toDomain(): Card =
        Card(
            cardId = cardId,
            cardName = cardName,
            cardQuestion = cardQuestion,
            cardAnswer = cardAnswer,
            cardType = CardType.fromStableId(cardType),
            cardTag = cardTag,
            deckId = deckId,
            cardState = enumValueOf<CardState>(cardState),
            easeFactor = easeFactor,
            interval = interval,
            learningStep = learningStep,
            nextReviewDate = nextReviewDate,
            repetitionCount = repetitionCount,
            lapseCount = lapseCount,
            firstReviewDate = firstReviewDate,
            lastReviewDate = lastReviewDate,
        )

    fun CardWithDeckEntity.toDomain(): CardWithDeck =
        CardWithDeck(
            card = card.toDomain(),
            deckId = deck.deckId,
            deckName = deck.deckName,
        )
}
