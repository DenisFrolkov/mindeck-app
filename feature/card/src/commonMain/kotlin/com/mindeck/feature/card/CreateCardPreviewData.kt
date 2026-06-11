package com.mindeck.feature.card

internal object CreateCardPreviewData {
    val idleContent =
        CreateCardUiState.Idle(
            question = "",
            answer = "",
        )

    val loading =
        CreateCardUiState.Loading

    val error = CreateCardUiState.Error("Не удалось загрузить колоды")
}
