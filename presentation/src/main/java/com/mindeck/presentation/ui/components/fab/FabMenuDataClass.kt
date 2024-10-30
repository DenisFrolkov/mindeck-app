package com.mindeck.presentation.ui.components.fab

import androidx.compose.ui.graphics.painter.Painter

data class FabMenuDataClass(
    val idItem: Int,
    val text: String,
    val icon: Painter,
    val iconContentDescription: String? = ""
)