package com.mindeck.feature.card.createCard

import com.mindeck.domain.models.Deck
import com.mindeck.feature.card.model.DeckItem

fun Deck.toUi() =
    DeckItem(
        id = deckId,
        title = deckName,
        deckColor = deckColor,
    )
