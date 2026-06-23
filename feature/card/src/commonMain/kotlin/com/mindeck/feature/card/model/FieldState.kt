package com.mindeck.feature.card.model

data class DeckFieldState(
    val deckPick: DeckPickState,
    val expanded: Boolean,
    val onPickDeck: (Int) -> Unit,
    val onClear: () -> Unit,
    val onCreateDeck: () -> Unit,
    val onExpand: () -> Unit,
)

data class CardTypeFieldState(
    val selectedType: CardType,
    val onSelectType: (CardType) -> Unit,
)

data class CardTextFieldState(
    val question: String,
    val onQuestionChange: (String) -> Unit,
    val answer: String,
    val onAnswerChange: (String) -> Unit,
    val hint: String,
    val onHintChange: (String) -> Unit,
    val activeFormats: Set<TextFormat>,
    val onToggleFormat: (TextFormat) -> Unit,
)

data class MediaFieldState(
    val selectedAudio: String?,
    val onAddPhoto: () -> Unit,
    val onAddAudio: () -> Unit,
    val onRemoveAudio: () -> Unit,
)

data class LinkFieldState(
    val draft: String,
    val onChange: (String) -> Unit,
    val onConfirm: () -> Unit,
)
