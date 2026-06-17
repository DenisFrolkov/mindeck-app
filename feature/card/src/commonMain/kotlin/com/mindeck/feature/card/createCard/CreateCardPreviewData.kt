package com.mindeck.feature.card.createCard

import com.mindeck.domain.models.DeckColor
import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.DeckItem

internal object CreateCardPreviewData {
    val idleContent =
        CreateCardUiState.Idle(
//            decks = emptyList(),
            decks =
                listOf(
                    DeckItem(
                        id = 1,
                        title = "Испанский базовая лексика",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 2,
                        title = "Английский фразовые глаголы",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 3,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 4,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 5,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 6,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 7,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 8,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 10,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                    DeckItem(
                        id = 11,
                        title = "Биология термины",
                        deckColor = DeckColor.BLUE,
                    ),
                ),
            //            pickedDeck = DeckItem(
//                id = 1,
//                title = "Испанский базовая лексика",
//                deckColor = DeckColor.BLUE,
//            ),
            selectedType = CardType.COMPLEX,
            question = "",
            answer = "",
        )

    val loading =
        CreateCardUiState.Loading

    val error = CreateCardUiState.Error("Не удалось загрузить колоды")
}
