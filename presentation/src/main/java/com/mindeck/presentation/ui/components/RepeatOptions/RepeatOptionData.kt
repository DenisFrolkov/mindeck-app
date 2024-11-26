package com.mindeck.presentation.ui.components.RepeatOptions

import androidx.compose.ui.graphics.Color

data class RepeatOptionData(
    var title: String,
    var time: String,
    var color: Color,
    val action: () -> Unit
)
