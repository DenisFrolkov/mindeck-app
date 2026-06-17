package com.mindeck.feature.card.createCard

import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.DeckItem
import com.mindeck.feature.card.model.DeckPickState

sealed interface CreateCardUiState {
    data object Loading : CreateCardUiState

    data class Error(
        val message: String,
    ) : CreateCardUiState

    data class Idle(
        val decks: List<DeckItem> = emptyList(),
        val pickedDeck: DeckItem? = null,
        val selectedType: CardType = CardType.SIMPLE,
        val question: String = "",
        val answer: String = "",
        val hint: String? = null,
    ) : CreateCardUiState {
        val deckPick: DeckPickState
            get() =
                when {
                    decks.isEmpty() -> DeckPickState.NoDecks
                    pickedDeck == null -> DeckPickState.NotSelected(decks = decks)
                    else -> DeckPickState.Selected(decks = decks, deck = pickedDeck)
                }
    }

    data object Success : CreateCardUiState
}

sealed interface CreateCardIntent {
    data class UpdateQuestion(
        val value: String,
    ) : CreateCardIntent

    data class UpdateAnswer(
        val value: String,
    ) : CreateCardIntent

    data class UpdateHint(
        val value: String,
    ) : CreateCardIntent

    data class SelectType(
        val type: CardType,
    ) : CreateCardIntent

    data class PickDeck(
        val deckId: Int,
    ) : CreateCardIntent

    data object ClearDeck : CreateCardIntent

    data object Submit : CreateCardIntent
}

sealed interface CreateCardNavigationEvent {
    data object Back : CreateCardNavigationEvent

    data object CreateDeck : CreateCardNavigationEvent
}
