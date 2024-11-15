package com.mindeck.presentation.ui.components.dropdown_selector

class DropdownSelectorDataClass(
    val title: String,
    val selectedItem: String,
    val itemList: List<String> = listOf(
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312"
    ),
    val onItemClick: (String) -> Unit
)