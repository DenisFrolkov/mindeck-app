package com.mindeck.feature.card.createCard

import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.DeckItem
import com.mindeck.feature.card.model.DeckPickState
import com.mindeck.feature.card.model.TextFormat

data class CreateCardState(
    val decks: List<DeckItem> = emptyList(),
    val pickedDeck: DeckItem? = null,
    val selectedType: CardType = CardType.SIMPLE,
    val question: String = "",
    val answer: String = "",
    val hint: String? = null,
    val activeFormats: Set<TextFormat> = emptySet(),
    val selectedAudio: String? = null,
    val isSubmitting: Boolean = false,
) {
    val deckPick: DeckPickState
        get() =
            when {
                decks.isEmpty() -> DeckPickState.NoDecks
                pickedDeck == null -> DeckPickState.NotSelected(decks = decks)
                else -> DeckPickState.Selected(decks = decks, deck = pickedDeck)
            }

    val canSubmit: Boolean
        get() = !isSubmitting && pickedDeck != null && question.isNotBlank() && answer.isNotBlank()
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

    data class ToggleFormat(
        val format: TextFormat,
    ) : CreateCardIntent

    data object RemoveAudio : CreateCardIntent

    data class PickDeck(
        val deckId: Int,
    ) : CreateCardIntent

    data object ClearDeck : CreateCardIntent

    data object Submit : CreateCardIntent
}

sealed interface CreateCardEffect {
    data object CardCreated : CreateCardEffect

    data class CreationFailed(
        val message: String,
    ) : CreateCardEffect
}

sealed interface CreateCardNavigationEvent {
    data object Back : CreateCardNavigationEvent

    data object CreateDeck : CreateCardNavigationEvent
}
