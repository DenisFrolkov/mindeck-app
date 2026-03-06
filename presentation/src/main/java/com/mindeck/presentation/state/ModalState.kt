package com.mindeck.presentation.state

sealed interface ModalState {
    data object None : ModalState
    data object DeckSelection : ModalState
    data object TypeSelection : ModalState
    data object DropdownMenu : ModalState
    data object RenameDialog : ModalState
    data object DeleteDialog : ModalState
}
