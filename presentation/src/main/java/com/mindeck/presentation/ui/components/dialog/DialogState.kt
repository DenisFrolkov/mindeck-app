package com.mindeck.presentation.ui.components.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mindeck.presentation.ui.components.dialog.data_class.DialogStateData
import com.mindeck.presentation.ui.components.dialog.data_class.DialogType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class DialogState(
    initialDialog: Boolean = false,
    val animateExpandedAlpha: Float = 1f,
    val scrimDialogAlpha: Float = 0.5f,
    val animationDuration: Int = 100,
) {
    var isDialogVisible by mutableStateOf(initialDialog)
        private set

    var currentDialogType by mutableStateOf<DialogType>(DialogType.None)
        private set

    var dialogStateData by mutableStateOf(DialogStateData())
        private set

    var isSelectItem = MutableStateFlow<Int?>(null)
        private set

    var isSelectingDecksForMoveAndDelete by mutableStateOf(false)
        private set

    var toastBooleanEvent by mutableStateOf(false)
        private set

    var toastTextEvent by mutableStateOf("")

    val toastAlpha: Float
        get() = if (toastBooleanEvent) animateExpandedAlpha else 0f

    private fun openDialog(dialogType: DialogType) {
        dialogStateData = DialogStateData()
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
        dialogStateData = dialogStateData.copy(isValid = folderName.isNotBlank())
        return folderName.isNotBlank()
    }

    fun updateSelectItem(folderId: Int?) {
        isSelectItem.value = folderId
    }

    fun updateDialogText(text: String) {
        dialogStateData = dialogStateData.copy(text = text)
    }

    fun toggleSelectingDecksForMoveAndDelete() {
        isSelectingDecksForMoveAndDelete = true
    }

    fun showErrorToast(message: String) {
        toastTextEvent = message
        toastBooleanEvent = true

    }

    suspend fun clearToast() {
        toastBooleanEvent = false
        delay(1000)
        toastTextEvent = ""
    }
}