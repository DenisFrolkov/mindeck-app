package com.mindeck.feature.card.createCard

import com.mindeck.core.mvi.BaseViewModel

class CreateCardViewModel : BaseViewModel<CreateCardUiState, CreateCardIntent>(CreateCardPreviewData.idleContent) {
    override fun accept(intent: CreateCardIntent) {
        when (intent) {
            is CreateCardIntent.UpdateQuestion ->
                updateIdle { it.copy(question = intent.value) }

            is CreateCardIntent.UpdateAnswer ->
                updateIdle { it.copy(answer = intent.value) }

            is CreateCardIntent.UpdateHint ->
                updateIdle { it.copy(hint = intent.value.ifBlank { null }) }

            is CreateCardIntent.SelectType ->
                updateIdle { it.copy(selectedType = intent.type) }

            CreateCardIntent.ClearDeck ->
                updateIdle { it.copy(pickedDeck = null) }

            is CreateCardIntent.PickDeck ->
                updateIdle { idle ->
                    idle.copy(pickedDeck = idle.decks.find { it.id == intent.deckId })
                }

            CreateCardIntent.Submit -> { }
        }
    }

    private fun updateIdle(reducer: (CreateCardUiState.Idle) -> CreateCardUiState.Idle) {
        updateState { state ->
            if (state is CreateCardUiState.Idle) reducer(state) else state
        }
    }
}
