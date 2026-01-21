package com.mindeck.presentation.ui.components.dropdown.dropdownSelector

class DropdownSelectorData(
    val title: String,
    val selectedItem: String,
    val itemList: List<Pair<String, Int>>,
    val onItemClick: (Pair<String, Int>) -> Unit,
)
