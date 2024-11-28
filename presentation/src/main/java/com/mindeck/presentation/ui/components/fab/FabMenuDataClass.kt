package com.mindeck.presentation.ui.components.fab

data class FabMenuDataClass(
    val idItem: Int,
    val text: String,
    val icon: Int,
    val iconContentDescription: String? = "",
    val navigation: () -> Unit
)