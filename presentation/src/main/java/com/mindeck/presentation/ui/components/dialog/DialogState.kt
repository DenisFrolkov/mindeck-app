package com.mindeck.presentation.ui.components.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class DialogState(
    initialDialog: Boolean = false,
    private val animateExpandedAlpha: Float = 1f,
    val scrimDialogAlpha: Float = 0.5f,
    val animationDuration: Int = 100
) {
    var isOpeningDialog by mutableStateOf(initialDialog)
        private set

    var validation by mutableStateOf<Boolean?>(null)
        private set

    var isEnterDialogText by mutableStateOf("")

    var isOpeningRenameDialog by mutableStateOf(false)
        private set

    var isOpeningCreateDialog by mutableStateOf(false)
        private set

    val dialogAlpha: Float
        get() = if (isOpeningDialog) animateExpandedAlpha else 0f

    fun openRenameDialog() {
        isOpeningRenameDialog = true
        isOpeningDialog = true
    }

    fun openCreateDialog() {
        isOpeningCreateDialog = true
        isOpeningDialog = true
    }

    fun openDialog() {
        isOpeningDialog = true
    }

    fun closeDialog() {
        validation = null
        isEnterDialogText = ""
        isOpeningDialog = false
        isOpeningRenameDialog = false
        isOpeningCreateDialog = false
    }

    fun validationCreate(folderName: String): Boolean {
        validation = folderName.isNotBlank()
        return validation == true
    }
}