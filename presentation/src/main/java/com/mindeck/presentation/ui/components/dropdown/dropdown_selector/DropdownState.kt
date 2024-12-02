package com.mindeck.presentation.ui.components.dropdown.dropdown_selector

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

class DropdownState(
    initialExpanded: Boolean = false,
    private val expandedHeight: Dp = MAX_VISIBLE_ITEMS * ITEM_HEIGHT.dp - 14.dp,
    private val animateOffsetY: Dp = 50.dp,
    private val animateAlpha: Float = 1f,
    val animationDuration: Int = 100
) {
    companion object {
        const val ITEM_HEIGHT = 36
        const val MAX_VISIBLE_ITEMS = 7
    }

    var isExpanded by mutableStateOf(initialExpanded)
        private set

    val dropdownHeight: Dp
        get() = if (isExpanded) expandedHeight else 0.dp

    val dropdownOffsetY: Dp
        get() = if (isExpanded) 0.dp else animateOffsetY

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

