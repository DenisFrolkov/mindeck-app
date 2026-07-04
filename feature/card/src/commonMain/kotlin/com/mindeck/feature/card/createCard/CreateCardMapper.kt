package com.mindeck.feature.card.createCard

import com.mindeck.domain.models.Deck
import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.DeckItem
import com.mindeck.domain.models.CardType as DomainCardType

fun Deck.toUi() =
    DeckItem(
        id = deckId,
        title = deckName,
        deckColor = deckColor,
    )

fun CardType.toDomain(): DomainCardType =
    when (this) {
        CardType.SIMPLE -> DomainCardType.SIMPLE
        CardType.COMPLEX -> DomainCardType.COMPLEX
    }
