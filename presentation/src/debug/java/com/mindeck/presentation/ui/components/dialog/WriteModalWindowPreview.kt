package com.mindeck.presentation.ui.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview
@Composable
private fun WriteModalWindowPreview() {
    MindeckTheme {
        WriteModalWindow(
            titleText = "Переименовать",
            buttonText = "Сохранить",
            placeholder = "Введите новое название...",
            actionState = UiState.Idle,
            onExitClick = {},
            onSaveClick = {},
        )
    }
}

@Preview
@Composable
private fun WriteModalWindowErrorPreview() {
    MindeckTheme {
        WriteModalWindow(
            titleText = "Переименовать",
            buttonText = "Сохранить",
            placeholder = "Введите новое название...",
            actionState = UiState.Error(R.string.error_deck_name_taken),
            onExitClick = {},
            onSaveClick = {},
        )
    }
}
