package com.mindeck.presentation.viewmodel.deck

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck

data class DeckScreenData(
    val deck: Deck,
    val cards: List<Card>,
)
