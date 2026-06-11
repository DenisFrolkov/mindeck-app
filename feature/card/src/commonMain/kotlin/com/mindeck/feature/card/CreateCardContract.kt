package com.mindeck.feature.card

sealed interface CreateCardUiState {
    data object Loading : CreateCardUiState

    data class Error(
        val message: String,
    ) : CreateCardUiState

    data class Idle(
        val question: String = "",
        val answer: String = "",
    ) : CreateCardUiState

    data object Success : CreateCardUiState
}

sealed interface CreateCardIntent {
    data class UpdateQuestion(
        val value: String,
    ) : CreateCardIntent

    data class UpdateAnswer(
        val value: String,
    ) : CreateCardIntent

    data object Submit : CreateCardIntent
}

sealed interface CreateCardNavigationEvent {
    data object Back : CreateCardNavigationEvent
}
