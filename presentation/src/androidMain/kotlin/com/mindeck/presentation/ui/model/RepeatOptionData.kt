package com.mindeck.presentation.ui.model

import androidx.compose.ui.graphics.Color

data class RepeatOptionData(
    val title: String,
    val time: String,
    val color: Color,
    val onClick: () -> Unit,
)
