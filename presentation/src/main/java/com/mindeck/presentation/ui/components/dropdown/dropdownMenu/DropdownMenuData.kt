package com.mindeck.presentation.ui.components.dropdown.dropdownMenu

import androidx.compose.ui.text.TextStyle

data class DropdownMenuData(
    var title: String,
    var titleStyle: TextStyle = TextStyle.Default,
    var action: () -> Unit,
)
