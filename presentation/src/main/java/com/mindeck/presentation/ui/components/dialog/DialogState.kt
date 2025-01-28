package com.mindeck.presentation.ui.components.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow

class DialogState(
    initialDialog: Boolean = false,
    private val animateExpandedAlpha: Float = 1f,
    val scrimDialogAlpha: Float = 0.5f,
    val animationDuration: Int = 100
) {
    var isOpeningDialog by mutableStateOf(initialDialog)
        private set

    var isOpeningMoveDialog by mutableStateOf(false)
        private set

    var isOpeningDeleteDialog by mutableStateOf(false)
        private set

    var isOpeningRenameDialog by mutableStateOf(false)
        private set

    var isOpenMoveItemsAndDeleteItem by mutableStateOf(false)
        private set

    var isOpeningCreateDialog by mutableStateOf(false)
        private set

    var isEnterDialogText by mutableStateOf("")

    var isSelectItem = MutableStateFlow<Int?>(null)
        private set

    var validation by mutableStateOf<Boolean?>(null)
        private set

    val dialogAlpha: Float
        get() = if (isOpeningDialog) animateExpandedAlpha else 0f

    fun toggleEditNameDialog() {
        validation = null
        isEnterDialogText = ""
        isOpeningRenameDialog = !isOpeningRenameDialog
        isOpeningDialog = !isOpeningDialog
    }

    fun toggleCreateDialog() {
        validation = null
        isEnterDialogText = ""
        isOpeningCreateDialog = !isOpeningCreateDialog
        isOpeningDialog = !isOpeningDialog
    }

    fun toggleMoveDialog() {
        isOpeningMoveDialog = !isOpeningMoveDialog
        isOpeningDialog = !isOpeningDialog
        isSelectItem.value = null
    }

    fun toggleMoveItemsAndDeleteItem() {
        isOpenMoveItemsAndDeleteItem = !isOpenMoveItemsAndDeleteItem
    }

    fun toggleDeleteItemDialog() {
        isOpeningDeleteDialog = !isOpeningDeleteDialog
        isOpeningDialog = !isOpeningDialog
        isSelectItem.value = null
    }

    fun validationCreateAndRename(folderName: String): Boolean {
        validation = folderName.isNotBlank()
        return validation == true
    }

    fun updateSelectItem(folderId: Int?) {
        isSelectItem.value = folderId
    }
}