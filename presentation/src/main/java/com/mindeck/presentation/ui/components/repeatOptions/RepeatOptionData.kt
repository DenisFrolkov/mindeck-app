package com.mindeck.presentation.ui.components.repeatOptions

import androidx.compose.ui.graphics.Color

data class RepeatOptionData(
    var title: String,
    var color: Color,
    val action: () -> Unit,
)
