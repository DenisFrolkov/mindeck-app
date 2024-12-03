package com.mindeck.presentation.ui.components.dropdown.dropdown_selector

class DropdownSelectorData(
    val title: String,
    val selectedItem: String,
    val itemList: List<String> = listOf(),
    val onItemClick: (String) -> Unit
)