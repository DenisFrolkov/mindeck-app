package com.mindeck.domain.models

data class Deck(
    val deckId: Int = 0,
    val deckName: String,
    val deckColor: DeckColor = DeckColor.BLUE
) {
    init {
        require(deckName.isNotBlank()) { "Deck name must not be blank" }
    }
}
