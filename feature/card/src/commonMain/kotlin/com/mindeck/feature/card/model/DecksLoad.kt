package com.mindeck.feature.card.model

sealed interface DecksLoad {
    data object Loading : DecksLoad

    data class Loaded(
        val decks: List<DeckItem>,
    ) : DecksLoad

    data object Failed : DecksLoad
}
