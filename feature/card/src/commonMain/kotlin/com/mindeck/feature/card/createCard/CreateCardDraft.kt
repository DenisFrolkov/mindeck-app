package com.mindeck.feature.card.createCard

import com.mindeck.feature.card.model.CardType
import com.mindeck.feature.card.model.TextFormat
import kotlinx.serialization.Serializable

@Serializable
data class CreateCardDraft(
    val question: String = "",
    val answer: String = "",
    val hint: String? = null,
    val selectedType: CardType = CardType.SIMPLE,
    val activeFormats: Set<TextFormat> = emptySet(),
)

fun CreateCardState.toDraft(): CreateCardDraft =
    CreateCardDraft(
        question = question,
        answer = answer,
        hint = hint,
        selectedType = selectedType,
        activeFormats = activeFormats,
    )

fun CreateCardDraft.toState(): CreateCardState =
    CreateCardState(
        question = question,
        answer = answer,
        hint = hint,
        selectedType = selectedType,
        activeFormats = activeFormats,
    )
