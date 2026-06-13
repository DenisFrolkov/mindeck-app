package com.mindeck.feature.card

import com.mindeck.core.mvi.BaseViewModel

class CreateCardViewModel : BaseViewModel<CreateCardUiState, CreateCardIntent>(CreateCardUiState.Idle()) {
    override fun accept(intent: CreateCardIntent) {
        when (intent) {
            is CreateCardIntent.UpdateQuestion ->
                updateIdle { it.copy(question = intent.value) }

            is CreateCardIntent.UpdateAnswer ->
                updateIdle { it.copy(answer = intent.value) }

            is CreateCardIntent.SelectType ->
                updateIdle { it.copy(selectedType = intent.type) }

            CreateCardIntent.Submit ->
                // Требует CreateCardUseCase + сбор Card из state и реальный deckId — отдельная задача.
                TODO("Submit: подключить CreateCardUseCase и навигацию после сохранения")
        }
    }

    private fun updateIdle(reducer: (CreateCardUiState.Idle) -> CreateCardUiState.Idle) {
        updateState { state ->
            if (state is CreateCardUiState.Idle) reducer(state) else state
        }
    }
}
