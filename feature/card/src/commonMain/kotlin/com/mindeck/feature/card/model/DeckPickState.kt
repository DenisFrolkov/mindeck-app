package com.mindeck.feature.card.model

sealed interface DeckPickState {
    data object NoDecks : DeckPickState

    data object NotSelected : DeckPickState

    data class Selected(
        val deck: DeckItem,
    ) : DeckPickState
}
