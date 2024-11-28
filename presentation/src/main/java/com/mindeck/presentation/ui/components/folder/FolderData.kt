package com.mindeck.presentation.ui.components.folder

import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.Color

data class FolderData(
    val id: Int,
    val countCard: Int,
    val text: String,
    val color: Color,
    val colorTwo: Color,
    val icon: Int
)
