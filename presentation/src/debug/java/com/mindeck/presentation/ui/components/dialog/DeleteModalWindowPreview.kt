package com.mindeck.presentation.ui.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview
@Composable
private fun DeleteModalWindowPreview() {
    MindeckTheme {
        DeleteModalWindow(
            titleText = "Удалить карточку",
            bodyText = "Вы уверены, что хотите удалить карточку \"Kotlin корутины\"?",
            actionState = UiState.Idle,
            onDeleteClick = {},
            onExitClick = {},
        )
    }
}

@Preview
@Composable
private fun DeleteModalWindowLoadingPreview() {
    MindeckTheme {
        DeleteModalWindow(
            titleText = "Удалить карточку",
            bodyText = "Вы уверены, что хотите удалить карточку \"Kotlin корутины\"?",
            actionState = UiState.Loading,
            onDeleteClick = {},
            onExitClick = {},
        )
    }
}
