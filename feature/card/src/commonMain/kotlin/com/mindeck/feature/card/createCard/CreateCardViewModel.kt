package com.mindeck.feature.card.createCard

import com.mindeck.core.mvi.BaseViewModel

class CreateCardViewModel : BaseViewModel<CreateCardState, CreateCardIntent, CreateCardEffect>(CreateCardPreviewData.idleContent) {
    override fun accept(intent: CreateCardIntent) {
        when (intent) {
            is CreateCardIntent.UpdateQuestion ->
                updateState { it.copy(question = intent.value) }

            is CreateCardIntent.UpdateAnswer ->
                updateState { it.copy(answer = intent.value) }

            is CreateCardIntent.UpdateHint ->
                updateState { it.copy(hint = intent.value.ifBlank { null }) }

            is CreateCardIntent.SelectType ->
                updateState { it.copy(selectedType = intent.type) }

            is CreateCardIntent.ToggleFormat ->
                updateState { state ->
                    val formats =
                        if (intent.format in state.activeFormats) {
                            state.activeFormats - intent.format
                        } else {
                            state.activeFormats + intent.format
                        }
                    state.copy(activeFormats = formats)
                }

            CreateCardIntent.RemoveAudio ->
                updateState { it.copy(selectedAudio = null) }

            CreateCardIntent.ClearDeck ->
                updateState { it.copy(pickedDeck = null) }

            is CreateCardIntent.PickDeck ->
                updateState { state ->
                    state.copy(pickedDeck = state.decks.find { it.id == intent.deckId })
                }

            CreateCardIntent.Submit -> submit()
        }
    }

    private fun submit() {
        if (!currentState.canSubmit) return
        // TODO: persist the card through the data layer (use case) once it is wired in.
        updateState {
            it.copy(
                isSubmitting = false,
                question = "",
                answer = "",
                hint = null,
                activeFormats = emptySet(),
                selectedAudio = null,
            )
        }
        sendEffect(CreateCardEffect.CardCreated)
    }
}
