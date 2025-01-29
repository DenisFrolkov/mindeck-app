package com.mindeck.presentation.ui.components.dialog

sealed interface DialogType {
    object None : DialogType
    object Create : DialogType
    object Rename : DialogType
    object Move : DialogType
    object Delete : DialogType
    object MoveItemsAndDeleteItem : DialogType
}