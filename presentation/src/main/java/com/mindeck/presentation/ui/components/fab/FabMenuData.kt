package com.mindeck.presentation.ui.components.fab

data class FabMenuData(
    val idItem: Int,
    val text: String,
    val icon: Int,
    val iconContentDescription: String? = "",
    val navigation: () -> Unit
)