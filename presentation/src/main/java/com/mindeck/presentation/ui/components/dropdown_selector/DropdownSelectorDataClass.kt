package com.mindeck.presentation.ui.components.dropdown_selector

class DropdownSelectorDataClass(
    val title: String,
    val selectedItem: String,
    val itemList: List<String> = listOf(),
    val onItemClick: (String) -> Unit
)