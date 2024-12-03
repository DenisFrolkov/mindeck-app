package com.mindeck.presentation.ui.components.dropdown.dropdown_menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class DropdownMenuState(
    initialExpanded: Boolean = false,
    private val animateAlpha: Float = 1f,
    val animationDuration: Int = 100
) {
    var isExpanded by mutableStateOf(initialExpanded)
        private set

    val dropdownAlpha: Float
        get() = if (isExpanded) animateAlpha else 0f

    fun open() {
        isExpanded = true
    }

    fun reset() {
        isExpanded = false
    }

    fun toggle() {
        if (isExpanded) reset() else open()
    }
}