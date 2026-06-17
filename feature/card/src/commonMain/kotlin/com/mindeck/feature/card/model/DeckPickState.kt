package com.mindeck.feature.card.model

sealed interface DeckPickState {
    data object NoDecks : DeckPickState

    data class NotSelected(
        val decks: List<DeckItem>
    ) : DeckPickState

    data class Selected(
        val decks: List<DeckItem>,
        val deck: DeckItem,
    ) : DeckPickState
}
