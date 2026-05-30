package com.mindeck.presentation.viewmodel.card

import androidx.annotation.StringRes

sealed interface CreationCardNavigationEvent {
    data class ShowToast(
        @StringRes val messageRes: Int,
    ) : CreationCardNavigationEvent
}
