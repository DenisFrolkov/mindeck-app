package com.mindeck.feature.card.createCard

import com.mindeck.domain.models.DeckColor
import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.DeckItem
import com.mindeck.feature.card.model.DeckPickState
import com.mindeck.feature.card.model.DraftImage
import com.mindeck.feature.card.model.MediaSheet
import com.mindeck.feature.card.model.TextFormat

data class CreateCardState(
    val decks: List<DeckItem> = emptyList(),
    val pickedDeck: DeckItem? = null,
    val selectedType: CardType = CardType.SIMPLE,
    val question: String = "",
    val answer: String = "",
    val hint: String? = null,
    val activeFormats: Set<TextFormat> = emptySet(),
    val draftImage: DraftImage? = null,
    val isProcessingImage: Boolean = false,
    val isSubmitting: Boolean = false,
    val isDeckPickerExpanded: Boolean = false,
    val isNewDeckDialogVisible: Boolean = false,
    val activeSheet: MediaSheet? = null,
    val isCameraVisible: Boolean = false,
    val linkDraft: String = "",
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
    data object ShowNewDeckDialog : CreateCardIntent

    data object DismissNewDeckDialog : CreateCardIntent

    data class CreateDeck(
        val name: String,
        val color: DeckColor,
    ) : CreateCardIntent

    data class SelectType(
        val type: CardType,
    ) : CreateCardIntent

    data class UpdateQuestion(
        val value: String,
    ) : CreateCardIntent

    data class UpdateAnswer(
        val value: String,
    ) : CreateCardIntent

    data class UpdateHint(
        val value: String,
    ) : CreateCardIntent

    data class ToggleFormat(
        val format: TextFormat,
    ) : CreateCardIntent

    data object RemoveImage : CreateCardIntent

    data object ShowAddPhotoSheet : CreateCardIntent

    data object DismissSheet : CreateCardIntent

    data object ShowCamera : CreateCardIntent

    data object DismissCamera : CreateCardIntent

    data object BeginImagePick : CreateCardIntent

    data class AttachImage(
        val image: DraftImage,
    ) : CreateCardIntent

    data object FailImagePick : CreateCardIntent

    data class UpdateLink(
        val value: String,
    ) : CreateCardIntent

    data object ConfirmLink : CreateCardIntent

    data class PickDeck(
        val deckId: Int,
    ) : CreateCardIntent

    data object ExpandDeckPicker : CreateCardIntent

    data object ClearDeck : CreateCardIntent

    data object Submit : CreateCardIntent
}

sealed interface CreateCardEffect {
    data object CardCreated : CreateCardEffect

    data class CreationFailed(
        val reason: CreateCardError,
    ) : CreateCardEffect
}

enum class CreateCardError {
    DeckNameTaken,
    ImageDownloadFailed,
    ImageAttachFailed,
    Unknown,
}

sealed interface CreateCardNavigationEvent {
    data object Back : CreateCardNavigationEvent
}
