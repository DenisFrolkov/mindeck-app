package com.mindeck.presentation.viewmodel.deck

import androidx.annotation.StringRes

sealed interface DeckNavigationEvent {
    data object GoBack : DeckNavigationEvent

    data class ShowToast(
        @StringRes val messageRes: Int,
    ) : DeckNavigationEvent
}
