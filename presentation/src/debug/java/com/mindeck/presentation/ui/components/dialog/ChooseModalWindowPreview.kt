package com.mindeck.presentation.ui.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.theme.MindeckTheme

@Preview
@Composable
private fun ChooseModalWindowListPreview() {
    MindeckTheme {
        ChooseModalWindow(
            titleText = "Выберите колоду",
            items = listOf(
                "Английский" to 1,
                "Математика" to 2,
                "История" to 3,
            ),
            selectedId = 1,
            actionState = UiState.Idle,
            showAddIcon = true,
            onExitClick = {},
            onSaveClick = {},
        )
    }
}

@Preview
@Composable
private fun ChooseModalWindowCreateModePreview() {
    MindeckTheme {
        ChooseModalWindow(
            titleText = "Выберите колоду",
            items = emptyList(),
            actionState = UiState.Idle,
            showAddIcon = true,
            createTitle = "Новая колода",
            createButtonLabel = "Создать",
            createPlaceholder = "Название колоды",
            onExitClick = {},
            onSaveClick = {},
        )
    }
}
