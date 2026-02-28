package com.mindeck.presentation.state

sealed interface ModalState {
    data object None : ModalState
    data object DeckSelection : ModalState
    data object TypeSelection : ModalState
}
