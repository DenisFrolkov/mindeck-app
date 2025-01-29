package com.mindeck.presentation.ui.components.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow

class DialogState(
    initialDialog: Boolean = false,
    val animateExpandedAlpha: Float = 1f,
    val scrimDialogAlpha: Float = 0.5f,
    val animationDuration: Int = 100
) {
    var isDialogVisible by mutableStateOf(initialDialog)
        private set

    var currentDialogType by mutableStateOf<DialogType>(DialogType.None)
        private set

    var dialogData by mutableStateOf(DialogData())
        private set

    var isSelectItem = MutableStateFlow<Int?>(null)
        private set

    var isSelectingDecksForMoveAndDelete by mutableStateOf(false)
        private set

    private fun openDialog(dialogType: DialogType) {
        dialogData = DialogData()
        currentDialogType = dialogType
        isDialogVisible = true
        if (dialogType != DialogType.Move) {
            isSelectItem.value = null
        }
    }

    fun closeDialog() {
        currentDialogType = DialogType.None
        isDialogVisible = false
        isSelectItem.value = null
    }

    fun openCreateDialog() {
        openDialog(DialogType.Create)
    }

    fun openRenameDialog() {
        openDialog(DialogType.Rename)
    }

    fun openDeleteDialog() {
        openDialog(DialogType.Delete)
    }

    fun openMoveDialog() {
        openDialog(DialogType.Move)
    }

    fun openMoveItemsAndDeleteItemDialog() {
        openDialog(DialogType.MoveItemsAndDeleteItem)
    }

    fun validateFolderName(folderName: String): Boolean {
        dialogData = dialogData.copy(isValid = folderName.isNotBlank())
        return dialogData.isValid == true
    }

    fun updateSelectItem(folderId: Int?) {
        isSelectItem.value = folderId
    }

    fun updateDialogText(text: String) {
        dialogData = dialogData.copy(text = text)
    }

    fun startSelectingDecksForMoveAndDelete() {
        isSelectingDecksForMoveAndDelete = true
    }

    fun stopSelectingDecksForMoveAndDelete() {
        isSelectingDecksForMoveAndDelete = false
    }
}