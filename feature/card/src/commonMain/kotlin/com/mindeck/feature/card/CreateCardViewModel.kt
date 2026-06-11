package com.mindeck.feature.card

import com.mindeck.core.mvi.BaseViewModel

class CreateCardViewModel : BaseViewModel<CreateCardUiState, CreateCardIntent>(CreateCardUiState.Idle()) {
    override fun accept(intent: CreateCardIntent) {
        when (intent) {
            CreateCardIntent.Submit -> TODO()
            is CreateCardIntent.UpdateAnswer -> TODO()
            is CreateCardIntent.UpdateQuestion -> TODO()
        }
    }
}
