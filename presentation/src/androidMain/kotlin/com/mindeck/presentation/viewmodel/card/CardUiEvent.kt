package com.mindeck.presentation.viewmodel.card

sealed interface CardUiEvent {
    data class DeletionSuccessful(
        val cardName: String,
    ) : CardUiEvent
}
