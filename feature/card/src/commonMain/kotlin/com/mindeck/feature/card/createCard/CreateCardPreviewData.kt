package com.mindeck.feature.card.createCard

import com.mindeck.domain.models.DeckColor
import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.DeckItem
import com.mindeck.feature.card.model.DecksLoad

internal object CreateCardPreviewData {
    val idleContent =
        CreateCardState(
            decks =
                DecksLoad.Loaded(
                    listOf(
                        DeckItem(id = 1, title = "Испанский базовая лексика", deckColor = DeckColor.BLUE),
                        DeckItem(id = 2, title = "Английский фразовые глаголы", deckColor = DeckColor.BLUE),
                        DeckItem(id = 3, title = "Биология термины", deckColor = DeckColor.BLUE),
                        DeckItem(id = 4, title = "История даты", deckColor = DeckColor.BLUE),
                        DeckItem(id = 5, title = "Химия формулы", deckColor = DeckColor.BLUE),
                    ),
                ),
            selectedType = CardType.COMPLEX,
        )
}
